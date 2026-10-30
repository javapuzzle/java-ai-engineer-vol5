package com.acme.retail.architecture.ch12;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GlobalRagPlatformGatewayTest {

    private GlobalRagPlatformGateway buildGateway() {
        NoisyNeighborGuard guard = new NoisyNeighborGuard(Map.of(
                "tenant-acme", new TenantCapPolicy("tenant-acme", 100),
                "tenant-enterprise", new TenantCapPolicy("tenant-enterprise", 100)));

        ShardedDocumentIndex euIndex = new ShardedDocumentIndex(new ShardRouter(4));
        euIndex.index(new IndexedDocument("doc-eu-1", "returns", "EU refund policy details."));

        ShardedDocumentIndex usIndex = new ShardedDocumentIndex(new ShardRouter(4));
        usIndex.index(new IndexedDocument("doc-us-1", "returns", "US refund policy details."));

        GlobalDeploymentRouter deploymentRouter = new GlobalDeploymentRouter(
                Map.of("tenant-enterprise", new OnPremiseModelDeploymentTarget("dc-frankfurt")),
                Map.of(
                        "eu", new CloudModelDeploymentTarget("eu"),
                        "us", new CloudModelDeploymentTarget("us")));

        return new GlobalRagPlatformGateway(
                guard,
                Map.of("eu", euIndex, "us", usIndex),
                deploymentRouter);
    }

    @Test
    void routesAnOrdinaryTenantsQueryToItsOwnRegionAndTheCloudDeployment() {
        GlobalRagPlatformGateway gateway = buildGateway();

        GlobalQueryResult result = gateway.handle(
                new GlobalQueryRequest("tenant-acme", "us", "returns", "refund", 10));

        assertThat(result.allowed()).isTrue();
        assertThat(result.matches()).extracting(IndexedDocument::id).containsExactly("doc-us-1");
        assertThat(result.shardsSearched()).isEqualTo(1);
        assertThat(result.servedByLocation()).isEqualTo("CLOUD:us");
    }

    @Test
    void routesADataResidencyTenantToItsOnPremiseDeploymentRegardlessOfRegion() {
        GlobalRagPlatformGateway gateway = buildGateway();

        GlobalQueryResult result = gateway.handle(
                new GlobalQueryRequest("tenant-enterprise", "eu", "returns", "refund", 10));

        assertThat(result.allowed()).isTrue();
        assertThat(result.matches()).extracting(IndexedDocument::id).containsExactly("doc-eu-1");
        assertThat(result.servedByLocation()).isEqualTo("ON_PREMISE:dc-frankfurt");
    }

    @Test
    void tenantIsolationBlocksAQueryBeforeAnyRegionOrDeploymentRoutingHappens() {
        GlobalRagPlatformGateway gateway = buildGateway();

        gateway.handle(new GlobalQueryRequest("tenant-acme", "us", "returns", "refund", 100));
        GlobalQueryResult blocked = gateway.handle(
                new GlobalQueryRequest("tenant-acme", "us", "returns", "refund", 1));

        assertThat(blocked.allowed()).isFalse();
        assertThat(blocked.matches()).isEmpty();
        assertThat(blocked.shardsSearched()).isZero();
        assertThat(blocked.servedByLocation()).isNull();
    }

    @Test
    void oneTenantBeingBlockedNeverAffectsAnotherTenantInADifferentRegion() {
        GlobalRagPlatformGateway gateway = buildGateway();

        gateway.handle(new GlobalQueryRequest("tenant-acme", "us", "returns", "refund", 100));
        GlobalQueryResult acmeBlocked = gateway.handle(
                new GlobalQueryRequest("tenant-acme", "us", "returns", "refund", 1));
        GlobalQueryResult enterpriseStillWorks = gateway.handle(
                new GlobalQueryRequest("tenant-enterprise", "eu", "returns", "refund", 100));

        assertThat(acmeBlocked.allowed()).isFalse();
        assertThat(enterpriseStillWorks.allowed()).isTrue();
        assertThat(enterpriseStillWorks.servedByLocation()).isEqualTo("ON_PREMISE:dc-frankfurt");
    }

    @Test
    void throwsForAQueryFromAnUnregisteredRegion() {
        GlobalRagPlatformGateway gateway = buildGateway();

        assertThatThrownBy(() -> gateway.handle(
                new GlobalQueryRequest("tenant-acme", "apac", "returns", "refund", 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("apac");
    }
}
