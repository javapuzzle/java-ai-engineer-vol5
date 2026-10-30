package com.acme.retail.architecture.ch17;

/**
 * A real back-of-envelope capacity calculator: a measured per-node
 * throughput, honestly adjusted for a different real average token
 * load, divided into the target request rate -- the same
 * "measured input, real computation" discipline this series has used
 * throughout, applied to infrastructure sizing.
 */
public class CapacityEstimator {

    public CapacityEstimate estimate(LoadTestResult loadTestResult, CapacityTarget target) {
        double tokenLoadRatio =
                loadTestResult.averageTokensPerRequestDuringTest() / target.targetAverageTokensPerRequest();
        double adjustedRequestsPerSecondPerNode =
                loadTestResult.sustainedRequestsPerSecondPerNode() * tokenLoadRatio;

        double exactNodesNeeded = target.targetRequestsPerSecond() / adjustedRequestsPerSecondPerNode;
        int requiredNodes = (int) Math.ceil(exactNodesNeeded);

        return new CapacityEstimate(requiredNodes, adjustedRequestsPerSecondPerNode);
    }
}
