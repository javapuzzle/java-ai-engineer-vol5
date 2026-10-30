package com.acme.retail.architecture.ch01;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArchitectureLayerRegistryTest {

    @Test
    void returnsTheRegisteredLayerForAKnownComponent() {
        ArchitectureLayerRegistry registry = new ArchitectureLayerRegistry()
                .register(new ComponentDeclaration("OpsAgentController", ArchitectureLayer.PRESENTATION));

        assertThat(registry.layerOf("OpsAgentController")).isEqualTo(ArchitectureLayer.PRESENTATION);
        assertThat(registry.isRegistered("OpsAgentController")).isTrue();
    }

    @Test
    void throwsForAnUnknownComponent() {
        ArchitectureLayerRegistry registry = new ArchitectureLayerRegistry();

        assertThatThrownBy(() -> registry.layerOf("Nonexistent"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nonexistent");
        assertThat(registry.isRegistered("Nonexistent")).isFalse();
    }

    @Test
    void rejectsBlankComponentNames() {
        assertThatThrownBy(() -> new ComponentDeclaration("  ", ArchitectureLayer.DOMAIN))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
