package com.acme.retail.architecture.ch19;

import java.util.Map;

/**
 * A real strangler-fig router: incrementally shifts a real percentage
 * of one component's traffic to its new path, deterministically per
 * request, so a migration can move from 0% to 100% in real, observable
 * steps instead of one risky, all-at-once cutover.
 */
public class StranglerFigRouter {

    private final Map<String, MigrationRolloutPolicy> policiesByComponent;

    public StranglerFigRouter(Map<String, MigrationRolloutPolicy> policiesByComponent) {
        this.policiesByComponent = Map.copyOf(policiesByComponent);
    }

    public RoutingTarget routeRequest(String componentName, String requestId) {
        MigrationRolloutPolicy policy = policiesByComponent.get(componentName);
        if (policy == null) {
            throw new IllegalArgumentException("No rollout policy registered for component: " + componentName);
        }

        int bucket = Math.floorMod(requestId.hashCode(), 100);
        return bucket < policy.rolloutPercentage() ? RoutingTarget.NEW_PATH : RoutingTarget.LEGACY_PATH;
    }
}
