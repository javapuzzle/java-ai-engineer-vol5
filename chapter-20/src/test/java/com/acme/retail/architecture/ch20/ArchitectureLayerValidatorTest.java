package com.acme.retail.architecture.ch20;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArchitectureLayerValidatorTest {

    private final ArchitectureLayerValidator validator = new ArchitectureLayerValidator();

    private ArchitectureLayerRegistry targetArchitectureRegistry() {
        return new ArchitectureLayerRegistry()
                .register(new ComponentDeclaration("TargetArchitectureGateway", ArchitectureLayer.ORCHESTRATION))
                .register(new ComponentDeclaration("NoisyNeighborGuard", ArchitectureLayer.GUARDRAIL))
                .register(new ComponentDeclaration("TargetArchitectureValidator", ArchitectureLayer.DOMAIN))
                .register(new ComponentDeclaration("RegionJurisdictionDirectory", ArchitectureLayer.INFRASTRUCTURE));
    }

    @Test
    void theCapstonesOwnComponentGraphHasNoLayeringViolations() {
        ArchitectureLayerRegistry registry = targetArchitectureRegistry();
        List<DependencyEdge> edges = List.of(
                new DependencyEdge("TargetArchitectureGateway", "NoisyNeighborGuard"),
                new DependencyEdge("NoisyNeighborGuard", "TargetArchitectureValidator"),
                new DependencyEdge("TargetArchitectureValidator", "RegionJurisdictionDirectory"));

        List<LayerViolation> violations = validator.validate(registry, edges);

        assertThat(violations).isEmpty();
    }

    @Test
    void stillCatchesADomainComponentDependingBackOnAGuardrail() {
        ArchitectureLayerRegistry registry = targetArchitectureRegistry();
        List<DependencyEdge> edges = List.of(
                new DependencyEdge("TargetArchitectureValidator", "NoisyNeighborGuard"));

        List<LayerViolation> violations = validator.validate(registry, edges);

        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).reason()).contains("DOMAIN may not depend on GUARDRAIL");
    }
}
