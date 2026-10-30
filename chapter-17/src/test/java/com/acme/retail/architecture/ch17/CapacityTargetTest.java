package com.acme.retail.architecture.ch17;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CapacityTargetTest {

    @Test
    void rejectsANonPositiveTargetRequestsPerSecond() {
        assertThatThrownBy(() -> new CapacityTarget(0, 300))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsANonPositiveTargetTokensPerRequest() {
        assertThatThrownBy(() -> new CapacityTarget(500, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void loadTestResultRejectsANonPositiveThroughput() {
        assertThatThrownBy(() -> new LoadTestResult(0, 300))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
