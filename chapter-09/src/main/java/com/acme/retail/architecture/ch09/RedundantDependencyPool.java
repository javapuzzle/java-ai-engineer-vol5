package com.acme.retail.architecture.ch09;

import java.util.List;
import java.util.function.Supplier;

/**
 * Real redundancy: a prioritized list of independent ways to satisfy the
 * same call. Each one is tried in order, and the first one that succeeds
 * wins -- redundancy that exists as actual, callable alternatives, not
 * just a diagram claiming a dependency is "redundant."
 */
public class RedundantDependencyPool {

    private final List<Supplier<String>> dependenciesInPriorityOrder;

    public RedundantDependencyPool(List<Supplier<String>> dependenciesInPriorityOrder) {
        if (dependenciesInPriorityOrder == null || dependenciesInPriorityOrder.isEmpty()) {
            throw new IllegalArgumentException("At least one dependency is required");
        }
        this.dependenciesInPriorityOrder = List.copyOf(dependenciesInPriorityOrder);
    }

    public String callWithRedundancy() {
        RuntimeException lastFailure = null;

        for (Supplier<String> dependency : dependenciesInPriorityOrder) {
            try {
                return dependency.get();
            } catch (RuntimeException failure) {
                lastFailure = failure;
            }
        }

        throw new IllegalStateException("All redundant dependencies failed", lastFailure);
    }
}
