package com.acme.retail.architecture.ch06;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TenantCapPolicyTest {

    @Test
    void rejectsABlankTenantId() {
        assertThatThrownBy(() -> new TenantCapPolicy(" ", 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsANonPositiveCap() {
        assertThatThrownBy(() -> new TenantCapPolicy("tenant-a", 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TenantCapPolicy("tenant-a", -5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsANonPositiveUsageRequest() {
        assertThatThrownBy(() -> new ResourceUsageRequest("tenant-a", 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
