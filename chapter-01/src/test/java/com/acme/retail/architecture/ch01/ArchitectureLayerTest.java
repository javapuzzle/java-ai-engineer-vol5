package com.acme.retail.architecture.ch01;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArchitectureLayerTest {

    @Test
    void layerAlwaysCanDependOnItself() {
        for (ArchitectureLayer layer : ArchitectureLayer.values()) {
            assertThat(layer.canDependOn(layer)).isTrue();
        }
    }

    @Test
    void presentationMayDependOnOrchestrationOnly() {
        assertThat(ArchitectureLayer.PRESENTATION.canDependOn(ArchitectureLayer.ORCHESTRATION)).isTrue();
        assertThat(ArchitectureLayer.PRESENTATION.canDependOn(ArchitectureLayer.DOMAIN)).isFalse();
        assertThat(ArchitectureLayer.PRESENTATION.canDependOn(ArchitectureLayer.GUARDRAIL)).isFalse();
        assertThat(ArchitectureLayer.PRESENTATION.canDependOn(ArchitectureLayer.INFRASTRUCTURE)).isFalse();
    }

    @Test
    void guardrailMayDependOnDomainAndBelow() {
        assertThat(ArchitectureLayer.GUARDRAIL.canDependOn(ArchitectureLayer.DOMAIN)).isTrue();
        assertThat(ArchitectureLayer.GUARDRAIL.canDependOn(ArchitectureLayer.MODEL_GATEWAY)).isTrue();
        assertThat(ArchitectureLayer.GUARDRAIL.canDependOn(ArchitectureLayer.INFRASTRUCTURE)).isTrue();
    }

    @Test
    void domainMustNeverDependOnGuardrail() {
        assertThat(ArchitectureLayer.DOMAIN.canDependOn(ArchitectureLayer.GUARDRAIL)).isFalse();
    }

    @Test
    void domainMayDependOnModelGatewayAndInfrastructure() {
        assertThat(ArchitectureLayer.DOMAIN.canDependOn(ArchitectureLayer.MODEL_GATEWAY)).isTrue();
        assertThat(ArchitectureLayer.DOMAIN.canDependOn(ArchitectureLayer.INFRASTRUCTURE)).isTrue();
    }

    @Test
    void infrastructureMayNotDependOnAnythingElse() {
        for (ArchitectureLayer layer : ArchitectureLayer.values()) {
            if (layer == ArchitectureLayer.INFRASTRUCTURE) {
                continue;
            }
            assertThat(ArchitectureLayer.INFRASTRUCTURE.canDependOn(layer)).isFalse();
        }
    }
}
