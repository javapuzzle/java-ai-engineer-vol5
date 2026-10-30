package com.acme.retail.architecture.ch07;

import java.util.Map;

/**
 * Assigns each tenant to the deployment target its actual requirements
 * demand -- most tenants to the shared cloud deployment, a tenant with a
 * hard data-residency requirement to its own on-premise target -- and
 * hands back a {@link ModelGatewayClient} already wired to the correct
 * one. Calling code asks the router for a client and never branches on
 * deployment location itself.
 */
public class HybridDeploymentRouter {

    private final Map<String, ModelDeploymentTarget> targetsByTenant;
    private final ModelDeploymentTarget defaultTarget;

    public HybridDeploymentRouter(
            Map<String, ModelDeploymentTarget> targetsByTenant, ModelDeploymentTarget defaultTarget) {
        this.targetsByTenant = Map.copyOf(targetsByTenant);
        this.defaultTarget = defaultTarget;
    }

    public ModelGatewayClient clientFor(String tenantId) {
        ModelDeploymentTarget target = targetsByTenant.getOrDefault(tenantId, defaultTarget);
        return new ModelGatewayClient(target);
    }

    public String locationFor(String tenantId) {
        return targetsByTenant.getOrDefault(tenantId, defaultTarget).locationName();
    }
}
