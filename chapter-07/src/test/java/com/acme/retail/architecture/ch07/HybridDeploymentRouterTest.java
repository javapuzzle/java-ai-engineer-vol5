package com.acme.retail.architecture.ch07;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HybridDeploymentRouterTest {

    private HybridDeploymentRouter router() {
        return new HybridDeploymentRouter(
                Map.of("tenant-enterprise", new OnPremiseModelDeploymentTarget("dc-frankfurt")),
                new CloudModelDeploymentTarget());
    }

    @Test
    void routesATenantWithADataResidencyRequirementToItsOwnDataCenter() {
        HybridDeploymentRouter router = router();

        ModelGatewayClient client = router.clientFor("tenant-enterprise");
        ModelInvocationResult result = client.invoke(
                new ModelInvocationRequest("tenant-enterprise", "Where is my shipment?"));

        assertThat(result.servedByLocation()).isEqualTo("ON_PREMISE:dc-frankfurt");
    }

    @Test
    void routesAnOrdinaryTenantToTheDefaultCloudDeployment() {
        HybridDeploymentRouter router = router();

        ModelGatewayClient client = router.clientFor("tenant-acme");
        ModelInvocationResult result = client.invoke(
                new ModelInvocationRequest("tenant-acme", "Where is my shipment?"));

        assertThat(result.servedByLocation()).isEqualTo("CLOUD");
    }

    @Test
    void locationForReflectsTheSameRoutingDecisionWithoutInvokingAModel() {
        HybridDeploymentRouter router = router();

        assertThat(router.locationFor("tenant-enterprise")).isEqualTo("ON_PREMISE:dc-frankfurt");
        assertThat(router.locationFor("tenant-acme")).isEqualTo("CLOUD");
    }

    @Test
    void callingCodeNeverBranchesOnDeploymentLocationItself() {
        HybridDeploymentRouter router = router();

        for (String tenantId : new String[] {"tenant-enterprise", "tenant-acme"}) {
            ModelGatewayClient client = router.clientFor(tenantId);
            ModelInvocationResult result = client.invoke(
                    new ModelInvocationRequest(tenantId, "Status check"));
            assertThat(result.responseText()).contains("Status check");
        }
    }
}
