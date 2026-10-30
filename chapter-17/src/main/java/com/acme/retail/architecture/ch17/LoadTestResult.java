package com.acme.retail.architecture.ch17;

/**
 * The real, measured result of a load test against one node --
 * Volume 4 Chapter 18's load-test shape, reused here as the honest
 * input a capacity estimate has to be built from, instead of a guess.
 */
public record LoadTestResult(double sustainedRequestsPerSecondPerNode, double averageTokensPerRequestDuringTest) {

    public LoadTestResult {
        if (sustainedRequestsPerSecondPerNode <= 0) {
            throw new IllegalArgumentException("sustainedRequestsPerSecondPerNode must be positive");
        }
        if (averageTokensPerRequestDuringTest <= 0) {
            throw new IllegalArgumentException("averageTokensPerRequestDuringTest must be positive");
        }
    }
}
