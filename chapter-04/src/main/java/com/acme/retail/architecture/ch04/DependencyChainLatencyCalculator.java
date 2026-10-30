package com.acme.retail.architecture.ch04;

import java.util.List;

/**
 * Computes the total latency of a chain of independent dependency calls
 * under two real strategies: calling them one after another (sequential),
 * where the total is the sum of every stage, and calling them all at
 * once (parallel), where the total is bounded by the single slowest
 * stage. This is the mechanism that shows parallelizing independent
 * sub-calls -- not just optimizing each one -- is what actually changes
 * the tail latency of the whole chain.
 */
public class DependencyChainLatencyCalculator {

    public long sequentialTotalMillis(List<Long> stageLatenciesMillis) {
        requireNonEmpty(stageLatenciesMillis);
        return stageLatenciesMillis.stream().mapToLong(Long::longValue).sum();
    }

    public long parallelTotalMillis(List<Long> stageLatenciesMillis) {
        requireNonEmpty(stageLatenciesMillis);
        return stageLatenciesMillis.stream().mapToLong(Long::longValue).max().orElseThrow();
    }

    private void requireNonEmpty(List<Long> stageLatenciesMillis) {
        if (stageLatenciesMillis == null || stageLatenciesMillis.isEmpty()) {
            throw new IllegalArgumentException("stageLatenciesMillis must not be empty");
        }
    }
}
