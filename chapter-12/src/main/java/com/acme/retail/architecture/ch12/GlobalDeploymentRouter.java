package com.acme.retail.architecture.ch12;

import java.util.Map;

/**
 * Composes Chapter 7's hybrid deployment routing with real region
 * awareness: a tenant with a data-residency override always routes to
 * its dedicated on-premise target regardless of region; every other
 * tenant routes to the cloud target for the region their request
 * actually came from.
 */
public class GlobalDeploymentRouter {

    private final Map<String, ModelDeploymentTarget> residencyOverridesByTenant;
    private final Map<String, ModelDeploymentTarget> cloudTargetsByRegion;

    public GlobalDeploymentRouter(
            Map<String, ModelDeploymentTarget> residencyOverridesByTenant,
            Map<String, ModelDeploymentTarget> cloudTargetsByRegion) {
        this.residencyOverridesByTenant = Map.copyOf(residencyOverridesByTenant);
        this.cloudTargetsByRegion = Map.copyOf(cloudTargetsByRegion);
    }

    public ModelDeploymentTarget targetFor(String tenantId, String region) {
        ModelDeploymentTarget override = residencyOverridesByTenant.get(tenantId);
        if (override != null) {
            return override;
        }

        ModelDeploymentTarget regional = cloudTargetsByRegion.get(region);
        if (regional == null) {
            throw new IllegalArgumentException("No cloud deployment target registered for region: " + region);
        }
        return regional;
    }

    public ModelGatewayClient clientFor(String tenantId, String region) {
        return new ModelGatewayClient(targetFor(tenantId, region));
    }
}
