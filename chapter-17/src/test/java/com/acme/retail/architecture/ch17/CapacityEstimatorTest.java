package com.acme.retail.architecture.ch17;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CapacityEstimatorTest {

    private final CapacityEstimator estimator = new CapacityEstimator();

    @Test
    void computesTheExactNodeCountWhenTheTargetTokenLoadMatchesTheLoadTest() {
        LoadTestResult loadTestResult = new LoadTestResult(50.0, 300.0);
        CapacityTarget target = new CapacityTarget(500.0, 300.0);

        CapacityEstimate estimate = estimator.estimate(loadTestResult, target);

        assertThat(estimate.adjustedRequestsPerSecondPerNode()).isCloseTo(50.0, within(0.0001));
        assertThat(estimate.requiredNodes()).isEqualTo(10);
    }

    @Test
    void doublingTheTargetTokenLoadDoublesTheRequiredNodeCount() {
        LoadTestResult loadTestResult = new LoadTestResult(50.0, 300.0);
        CapacityTarget heavierTarget = new CapacityTarget(500.0, 600.0);

        CapacityEstimate estimate = estimator.estimate(loadTestResult, heavierTarget);

        assertThat(estimate.adjustedRequestsPerSecondPerNode()).isCloseTo(25.0, within(0.0001));
        assertThat(estimate.requiredNodes()).isEqualTo(20);
    }

    @Test
    void roundsAFractionalNodeRequirementUpToTheNextWholeNode() {
        LoadTestResult loadTestResult = new LoadTestResult(50.0, 300.0);
        CapacityTarget target = new CapacityTarget(501.0, 300.0);

        CapacityEstimate estimate = estimator.estimate(loadTestResult, target);

        // 501 / 50 = 10.02 -> must round up to 11, never truncate to 10.
        assertThat(estimate.requiredNodes()).isEqualTo(11);
    }

    @Test
    void aLighterTargetTokenLoadReducesTheRequiredNodeCount() {
        LoadTestResult loadTestResult = new LoadTestResult(50.0, 300.0);
        CapacityTarget lighterTarget = new CapacityTarget(500.0, 150.0);

        CapacityEstimate estimate = estimator.estimate(loadTestResult, lighterTarget);

        assertThat(estimate.adjustedRequestsPerSecondPerNode()).isCloseTo(100.0, within(0.0001));
        assertThat(estimate.requiredNodes()).isEqualTo(5);
    }
}
