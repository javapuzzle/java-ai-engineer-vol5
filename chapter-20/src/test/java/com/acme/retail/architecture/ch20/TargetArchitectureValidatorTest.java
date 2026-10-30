package com.acme.retail.architecture.ch20;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetArchitectureValidatorTest {

    private final RegionJurisdictionDirectory jurisdictions = new RegionJurisdictionDirectory(Map.of(
            "eu-frankfurt", "EU",
            "eu-dublin", "EU",
            "us-east", "US"));

    private final TargetArchitectureValidator validator = new TargetArchitectureValidator(jurisdictions);

    @Test
    void reportsNoConflictWhenTheFailoverBackupStaysInTheRequiredJurisdiction() {
        Map<String, ResidencyRequirement> residency = Map.of(
                "tenant-enterprise", new ResidencyRequirement("tenant-enterprise", "EU"));
        Map<String, String> assignment = Map.of("tenant-enterprise", "eu-frankfurt");
        RegionFailoverPlan failoverPlan = new RegionFailoverPlan(Map.of(
                "eu-frankfurt", new RegionFailoverPolicy("eu-frankfurt", "eu-dublin")));

        List<ResidencyConflict> conflicts =
                validator.findResidencyFailoverConflicts(residency, assignment, failoverPlan);

        assertThat(conflicts).isEmpty();
    }

    @Test
    void catchesARegionalFailoverThatWouldMoveResidencyBoundDataAcrossAJurisdiction() {
        Map<String, ResidencyRequirement> residency = Map.of(
                "tenant-enterprise", new ResidencyRequirement("tenant-enterprise", "EU"));
        Map<String, String> assignment = Map.of("tenant-enterprise", "eu-frankfurt");
        // The disaster-recovery failover plan (Chapters 9-10) sends eu-frankfurt's
        // traffic to us-east during an outage -- correct as a failover plan,
        // but never checked against tenant-enterprise's EU residency requirement.
        RegionFailoverPlan conflictingFailoverPlan = new RegionFailoverPlan(Map.of(
                "eu-frankfurt", new RegionFailoverPolicy("eu-frankfurt", "us-east")));

        List<ResidencyConflict> conflicts =
                validator.findResidencyFailoverConflicts(residency, assignment, conflictingFailoverPlan);

        assertThat(conflicts).hasSize(1);
        ResidencyConflict conflict = conflicts.get(0);
        assertThat(conflict.tenantId()).isEqualTo("tenant-enterprise");
        assertThat(conflict.requiredJurisdiction()).isEqualTo("EU");
        assertThat(conflict.failoverRegion()).isEqualTo("us-east");
        assertThat(conflict.failoverJurisdiction()).isEqualTo("US");
        assertThat(conflict.reason()).contains("tenant-enterprise").contains("EU");
    }

    @Test
    void theConflictDisappearsOnceTheFailoverBackupIsMovedToTheSameJurisdiction() {
        Map<String, ResidencyRequirement> residency = Map.of(
                "tenant-enterprise", new ResidencyRequirement("tenant-enterprise", "EU"));
        Map<String, String> assignment = Map.of("tenant-enterprise", "eu-frankfurt");
        RegionFailoverPlan fixedFailoverPlan = new RegionFailoverPlan(Map.of(
                "eu-frankfurt", new RegionFailoverPolicy("eu-frankfurt", "eu-dublin")));

        List<ResidencyConflict> conflicts =
                validator.findResidencyFailoverConflicts(residency, assignment, fixedFailoverPlan);

        assertThat(conflicts).isEmpty();
    }

    @Test
    void throwsWhenATenantsAssignedRegionContradictsItsOwnResidencyRequirement() {
        Map<String, ResidencyRequirement> residency = Map.of(
                "tenant-enterprise", new ResidencyRequirement("tenant-enterprise", "EU"));
        Map<String, String> inconsistentAssignment = Map.of("tenant-enterprise", "us-east");
        RegionFailoverPlan failoverPlan = new RegionFailoverPlan(Map.of(
                "us-east", new RegionFailoverPolicy("us-east", "us-east")));

        assertThatThrownBy(() -> validator.findResidencyFailoverConflicts(
                residency, inconsistentAssignment, failoverPlan))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tenant-enterprise");
    }

    @Test
    void throwsWhenATenantWithAResidencyRequirementHasNoRegionAssignmentAtAll() {
        Map<String, ResidencyRequirement> residency = Map.of(
                "tenant-enterprise", new ResidencyRequirement("tenant-enterprise", "EU"));
        RegionFailoverPlan failoverPlan = new RegionFailoverPlan(Map.of(
                "eu-frankfurt", new RegionFailoverPolicy("eu-frankfurt", "eu-dublin")));

        assertThatThrownBy(() -> validator.findResidencyFailoverConflicts(
                residency, Map.of(), failoverPlan))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tenant-enterprise");
    }
}
