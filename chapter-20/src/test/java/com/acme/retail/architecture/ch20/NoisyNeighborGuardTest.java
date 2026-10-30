package com.acme.retail.architecture.ch20;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class NoisyNeighborGuardTest {

    @Test
    void allowsUsageWithinCapAndBlocksOverIt() {
        NoisyNeighborGuard guard = new NoisyNeighborGuard(Map.of(
                "tenant-enterprise", new TenantCapPolicy("tenant-enterprise", 100)));

        IsolationDecision within = guard.evaluate(new ResourceUsageRequest("tenant-enterprise", 90));
        IsolationDecision over = guard.evaluate(new ResourceUsageRequest("tenant-enterprise", 20));

        assertThat(within.allowed()).isTrue();
        assertThat(over.allowed()).isFalse();
    }
}
