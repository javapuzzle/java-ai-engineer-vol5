package com.acme.retail.architecture.ch20;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TargetArchitectureGatewayTest {

    private final RegionJurisdictionDirectory jurisdictions = new RegionJurisdictionDirectory(Map.of(
            "eu-frankfurt", "EU",
            "eu-dublin", "EU",
            "us-east", "US"));

    private TargetArchitectureGateway buildGateway(RegionFailoverPlan failoverPlan) {
        NoisyNeighborGuard tenantGuard = new NoisyNeighborGuard(Map.of(
                "tenant-enterprise", new TenantCapPolicy("tenant-enterprise", 100),
                "tenant-acme", new TenantCapPolicy("tenant-acme", 100)));

        TargetArchitectureValidator validator = new TargetArchitectureValidator(jurisdictions);

        Map<String, ResidencyRequirement> residency = Map.of(
                "tenant-enterprise", new ResidencyRequirement("tenant-enterprise", "EU"));

        Map<String, String> assignment = Map.of(
                "tenant-enterprise", "eu-frankfurt",
                "tenant-acme", "us-east");

        return new TargetArchitectureGateway(tenantGuard, validator, residency, assignment, failoverPlan);
    }

    @Test
    void aTenantWithNoResidencyRequirementIsAllowedOnceIsolationPasses() {
        RegionFailoverPlan failoverPlan = new RegionFailoverPlan(Map.of(
                "us-east", new RegionFailoverPolicy("us-east", "us-east")));
        TargetArchitectureGateway gateway = buildGateway(failoverPlan);

        TargetArchitectureDecision decision =
                gateway.evaluate(new TargetArchitectureRequest("tenant-acme", "us-east", 10));

        assertThat(decision.allowed()).isTrue();
        assertThat(decision.residencyConflicts()).isEmpty();
    }

    @Test
    void isolationIsCheckedFirstAndBlocksBeforeAnyResidencyCheckEverRuns() {
        RegionFailoverPlan failoverPlan = new RegionFailoverPlan(Map.of(
                "eu-frankfurt", new RegionFailoverPolicy("eu-frankfurt", "us-east")));
        TargetArchitectureGateway gateway = buildGateway(failoverPlan);

        gateway.evaluate(new TargetArchitectureRequest("tenant-enterprise", "eu-frankfurt", 100));
        TargetArchitectureDecision overCap =
                gateway.evaluate(new TargetArchitectureRequest("tenant-enterprise", "eu-frankfurt", 1));

        assertThat(overCap.allowed()).isFalse();
        assertThat(overCap.reason()).contains("tenant-enterprise").contains("100");
        assertThat(overCap.residencyConflicts()).isEmpty();
    }

    @Test
    void theComposedGatewayCatchesTheLiveResidencyFailoverConflict() {
        // Regional failover (Chapters 9-10) is individually correct: it will
        // successfully move eu-frankfurt's traffic to us-east during an outage.
        // Residency-aware deployment routing (Chapter 7) is individually correct
        // too. Composed, nothing before this chapter ever checked the two
        // against each other -- exactly the gap this capstone's validator closes.
        RegionFailoverPlan conflictingFailoverPlan = new RegionFailoverPlan(Map.of(
                "eu-frankfurt", new RegionFailoverPolicy("eu-frankfurt", "us-east")));
        TargetArchitectureGateway gateway = buildGateway(conflictingFailoverPlan);

        TargetArchitectureDecision decision =
                gateway.evaluate(new TargetArchitectureRequest("tenant-enterprise", "eu-frankfurt", 10));

        assertThat(decision.allowed()).isFalse();
        assertThat(decision.reason()).isEqualTo("Residency/failover conflict detected");
        assertThat(decision.residencyConflicts()).hasSize(1);
        assertThat(decision.residencyConflicts().get(0).failoverJurisdiction()).isEqualTo("US");
    }

    @Test
    void theSameTenantIsAllowedOnceTheFailoverPlanIsFixedToStayInJurisdiction() {
        RegionFailoverPlan fixedFailoverPlan = new RegionFailoverPlan(Map.of(
                "eu-frankfurt", new RegionFailoverPolicy("eu-frankfurt", "eu-dublin")));
        TargetArchitectureGateway gateway = buildGateway(fixedFailoverPlan);

        TargetArchitectureDecision decision =
                gateway.evaluate(new TargetArchitectureRequest("tenant-enterprise", "eu-frankfurt", 10));

        assertThat(decision.allowed()).isTrue();
        assertThat(decision.residencyConflicts()).isEmpty();
    }
}
