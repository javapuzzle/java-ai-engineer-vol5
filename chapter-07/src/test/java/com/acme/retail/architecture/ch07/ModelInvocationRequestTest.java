package com.acme.retail.architecture.ch07;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModelInvocationRequestTest {

    @Test
    void rejectsABlankTenantId() {
        assertThatThrownBy(() -> new ModelInvocationRequest(" ", "prompt"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsABlankPrompt() {
        assertThatThrownBy(() -> new ModelInvocationRequest("tenant-acme", " "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void onPremiseTargetRejectsABlankDataCenterId() {
        assertThatThrownBy(() -> new OnPremiseModelDeploymentTarget(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
