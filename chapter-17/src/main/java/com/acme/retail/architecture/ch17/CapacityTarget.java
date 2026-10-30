package com.acme.retail.architecture.ch17;

/**
 * The real, projected traffic a deployment actually has to serve --
 * a target throughput at a target average token load, which may be
 * genuinely different from whatever profile the original load test
 * measured.
 */
public record CapacityTarget(double targetRequestsPerSecond, double targetAverageTokensPerRequest) {

    public CapacityTarget {
        if (targetRequestsPerSecond <= 0) {
            throw new IllegalArgumentException("targetRequestsPerSecond must be positive");
        }
        if (targetAverageTokensPerRequest <= 0) {
            throw new IllegalArgumentException("targetAverageTokensPerRequest must be positive");
        }
    }
}
