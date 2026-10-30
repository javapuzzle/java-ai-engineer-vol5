package com.acme.retail.architecture.ch11;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequirementConstraintMapperTest {

    private final RequirementConstraintMapper mapper = new RequirementConstraintMapper(List.of(
            new LatencyRequirementRule(),
            new AvailabilityRequirementRule(),
            new DataResidencyRequirementRule()));

    @Test
    void derivesALatencyConstraintFromALatencyRequirement() {
        Requirement requirement = new Requirement("REQ-1", "Must respond within 200ms at P99.");

        List<ArchitecturalConstraint> constraints = mapper.deriveConstraints(List.of(requirement));

        assertThat(constraints).hasSize(1);
        assertThat(constraints.get(0).category()).isEqualTo(ConstraintCategory.LATENCY);
        assertThat(constraints.get(0).requirementId()).isEqualTo("REQ-1");
    }

    @Test
    void derivesAnAvailabilityConstraintFromAnUptimeRequirement() {
        Requirement requirement = new Requirement("REQ-2", "Must be available 99.99% of the time.");

        List<ArchitecturalConstraint> constraints = mapper.deriveConstraints(List.of(requirement));

        assertThat(constraints).hasSize(1);
        assertThat(constraints.get(0).category()).isEqualTo(ConstraintCategory.AVAILABILITY);
    }

    @Test
    void derivesADataResidencyConstraintFromAResidencyRequirement() {
        Requirement requirement = new Requirement("REQ-3", "Customer data must never leave the EU.");

        List<ArchitecturalConstraint> constraints = mapper.deriveConstraints(List.of(requirement));

        assertThat(constraints).hasSize(1);
        assertThat(constraints.get(0).category()).isEqualTo(ConstraintCategory.DATA_RESIDENCY);
    }

    @Test
    void aRequirementMatchingNoRuleProducesNoConstraintsAndIsReportedAsUnaddressed() {
        Requirement vague = new Requirement("REQ-4", "The dashboard should look modern.");

        List<ArchitecturalConstraint> constraints = mapper.deriveConstraints(List.of(vague));
        List<Requirement> unaddressed = mapper.unaddressedRequirements(List.of(vague));

        assertThat(constraints).isEmpty();
        assertThat(unaddressed).containsExactly(vague);
    }

    @Test
    void aRequirementMatchingMultipleRulesProducesMultipleConstraints() {
        Requirement requirement = new Requirement(
                "REQ-5", "Must respond within 200ms and never let data leave the data center.");

        List<ArchitecturalConstraint> constraints = mapper.deriveConstraints(List.of(requirement));

        assertThat(constraints).extracting(ArchitecturalConstraint::category)
                .containsExactlyInAnyOrder(ConstraintCategory.LATENCY, ConstraintCategory.DATA_RESIDENCY);
    }

    @Test
    void unaddressedRequirementsOnlyContainsRequirementsMatchingNoRule() {
        Requirement latencyReq = new Requirement("REQ-1", "Must respond within 200ms at P99.");
        Requirement vagueReq = new Requirement("REQ-4", "The dashboard should look modern.");

        List<Requirement> unaddressed = mapper.unaddressedRequirements(List.of(latencyReq, vagueReq));

        assertThat(unaddressed).containsExactly(vagueReq);
    }
}
