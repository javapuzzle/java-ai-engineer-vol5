package com.acme.retail.architecture.ch17;

/**
 * The real, computed sizing result: how many nodes the target traffic
 * actually requires, and the per-node throughput that number was
 * derived from, once adjusted for the target's real token load.
 */
public record CapacityEstimate(int requiredNodes, double adjustedRequestsPerSecondPerNode) {
}
