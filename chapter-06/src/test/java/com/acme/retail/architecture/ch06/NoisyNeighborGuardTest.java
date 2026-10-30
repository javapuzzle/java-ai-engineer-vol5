package com.acme.retail.architecture.ch06;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoisyNeighborGuardTest {

    private NoisyNeighborGuard guardFor(long capA, long capB) {
        return new NoisyNeighborGuard(Map.of(
                "tenant-a", new TenantCapPolicy("tenant-a", capA),
                "tenant-b", new TenantCapPolicy("tenant-b", capB)));
    }

    @Test
    void allowsUsageWithinTheTenantsCap() {
        NoisyNeighborGuard guard = guardFor(100, 100);

        IsolationDecision decision = guard.evaluate(new ResourceUsageRequest("tenant-a", 40));

        assertThat(decision.allowed()).isTrue();
        assertThat(decision.cumulativeUnitsUsed()).isEqualTo(40);
    }

    @Test
    void blocksUsageThatWouldExceedTheTenantsCap() {
        NoisyNeighborGuard guard = guardFor(100, 100);
        guard.evaluate(new ResourceUsageRequest("tenant-a", 90));

        IsolationDecision decision = guard.evaluate(new ResourceUsageRequest("tenant-a", 20));

        assertThat(decision.allowed()).isFalse();
        assertThat(decision.reason()).contains("tenant-a").contains("100");
        assertThat(guard.cumulativeUsageFor("tenant-a")).isEqualTo(90);
    }

    @Test
    void oneTenantMaxingOutItsCapNeverAffectsAnotherTenantsRequests() {
        NoisyNeighborGuard guard = guardFor(100, 100);

        guard.evaluate(new ResourceUsageRequest("tenant-a", 100));
        IsolationDecision tenantAOverflow = guard.evaluate(new ResourceUsageRequest("tenant-a", 1));
        IsolationDecision tenantBRequest = guard.evaluate(new ResourceUsageRequest("tenant-b", 100));

        assertThat(tenantAOverflow.allowed()).isFalse();
        assertThat(tenantBRequest.allowed()).isTrue();
        assertThat(guard.cumulativeUsageFor("tenant-b")).isEqualTo(100);
    }

    @Test
    void throwsForATenantWithNoRegisteredCapPolicy() {
        NoisyNeighborGuard guard = guardFor(100, 100);

        assertThatThrownBy(() -> guard.evaluate(new ResourceUsageRequest("tenant-unregistered", 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tenant-unregistered");
    }

    @Test
    void allowsUsageExactlyAtTheCapBoundary() {
        NoisyNeighborGuard guard = guardFor(100, 100);

        IsolationDecision decision = guard.evaluate(new ResourceUsageRequest("tenant-a", 100));

        assertThat(decision.allowed()).isTrue();
        assertThat(decision.cumulativeUnitsUsed()).isEqualTo(100);
    }
}
