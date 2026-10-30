package com.acme.retail.architecture.ch01;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArchitectureLayerValidatorTest {

    private final ArchitectureLayerValidator validator = new ArchitectureLayerValidator();

    private ArchitectureLayerRegistry acmeOpsAgentRegistry() {
        return new ArchitectureLayerRegistry()
                .register(new ComponentDeclaration("OpsAgentController", ArchitectureLayer.PRESENTATION))
                .register(new ComponentDeclaration("OpsAgentOrchestrator", ArchitectureLayer.ORCHESTRATION))
                .register(new ComponentDeclaration("CostBudgetGuardrail", ArchitectureLayer.GUARDRAIL))
                .register(new ComponentDeclaration("RefundPolicy", ArchitectureLayer.DOMAIN))
                .register(new ComponentDeclaration("ModelGatewayClient", ArchitectureLayer.MODEL_GATEWAY))
                .register(new ComponentDeclaration("PricingDatabase", ArchitectureLayer.INFRASTRUCTURE));
    }

    @Test
    void reportsNoViolationsForACorrectlyLayeredGraph() {
        ArchitectureLayerRegistry registry = acmeOpsAgentRegistry();
        List<DependencyEdge> edges = List.of(
                new DependencyEdge("OpsAgentController", "OpsAgentOrchestrator"),
                new DependencyEdge("OpsAgentOrchestrator", "CostBudgetGuardrail"),
                new DependencyEdge("CostBudgetGuardrail", "RefundPolicy"),
                new DependencyEdge("RefundPolicy", "ModelGatewayClient"),
                new DependencyEdge("ModelGatewayClient", "PricingDatabase"));

        List<LayerViolation> violations = validator.validate(registry, edges);

        assertThat(violations).isEmpty();
    }

    @Test
    void catchesDomainDependingBackOnGuardrail() {
        ArchitectureLayerRegistry registry = acmeOpsAgentRegistry();
        List<DependencyEdge> edges = List.of(
                new DependencyEdge("OpsAgentOrchestrator", "CostBudgetGuardrail"),
                new DependencyEdge("RefundPolicy", "CostBudgetGuardrail"));

        List<LayerViolation> violations = validator.validate(registry, edges);

        assertThat(violations).hasSize(1);
        LayerViolation violation = violations.get(0);
        assertThat(violation.fromComponent()).isEqualTo("RefundPolicy");
        assertThat(violation.fromLayer()).isEqualTo(ArchitectureLayer.DOMAIN);
        assertThat(violation.toComponent()).isEqualTo("CostBudgetGuardrail");
        assertThat(violation.toLayer()).isEqualTo(ArchitectureLayer.GUARDRAIL);
        assertThat(violation.reason()).contains("DOMAIN may not depend on GUARDRAIL");
    }

    @Test
    void catchesPresentationSkippingStraightToDomain() {
        ArchitectureLayerRegistry registry = acmeOpsAgentRegistry();
        List<DependencyEdge> edges = List.of(
                new DependencyEdge("OpsAgentController", "RefundPolicy"));

        List<LayerViolation> violations = validator.validate(registry, edges);

        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).fromComponent()).isEqualTo("OpsAgentController");
        assertThat(violations.get(0).toComponent()).isEqualTo("RefundPolicy");
    }

    @Test
    void reportsEveryViolationInAGraphWithMultiple() {
        ArchitectureLayerRegistry registry = acmeOpsAgentRegistry();
        List<DependencyEdge> edges = List.of(
                new DependencyEdge("RefundPolicy", "CostBudgetGuardrail"),
                new DependencyEdge("PricingDatabase", "ModelGatewayClient"));

        List<LayerViolation> violations = validator.validate(registry, edges);

        assertThat(violations).hasSize(2);
    }

    @Test
    void throwsWhenAnEdgeReferencesAnUnregisteredComponent() {
        ArchitectureLayerRegistry registry = acmeOpsAgentRegistry();
        List<DependencyEdge> edges = List.of(
                new DependencyEdge("OpsAgentController", "SomeUndeclaredService"));

        assertThatThrownBy(() -> validator.validate(registry, edges))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SomeUndeclaredService");
    }
}
