package com.acme.retail.architecture.ch09;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CapabilityTierSelectorTest {

    private final CapabilityTierSelector selector = new CapabilityTierSelector();

    @Test
    void aHighHealthScoreSelectsFullCapability() {
        assertThat(selector.select(0.99)).isEqualTo(CapabilityTier.FULL_CAPABILITY);
        assertThat(selector.select(0.95)).isEqualTo(CapabilityTier.FULL_CAPABILITY);
    }

    @Test
    void aMidRangeHealthScoreSelectsDegradedCapability() {
        assertThat(selector.select(0.94)).isEqualTo(CapabilityTier.DEGRADED_CAPABILITY);
        assertThat(selector.select(0.5)).isEqualTo(CapabilityTier.DEGRADED_CAPABILITY);
    }

    @Test
    void aLowHealthScoreSelectsSafeFallback() {
        assertThat(selector.select(0.49)).isEqualTo(CapabilityTier.SAFE_FALLBACK);
        assertThat(selector.select(0.0)).isEqualTo(CapabilityTier.SAFE_FALLBACK);
    }

    @Test
    void rejectsAnOutOfRangeHealthScore() {
        assertThatThrownBy(() -> selector.select(-0.1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> selector.select(1.1)).isInstanceOf(IllegalArgumentException.class);
    }
}
