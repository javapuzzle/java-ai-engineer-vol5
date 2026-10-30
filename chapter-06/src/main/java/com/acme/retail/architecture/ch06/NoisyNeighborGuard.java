package com.acme.retail.architecture.ch06;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A real, cumulative-state guardrail enforced outside any single
 * request's own reasoning -- the same shape as Volume 3 Chapter 18's
 * refund guardrail and Volume 4 Chapter 4's cost guardrail, now applied
 * to per-tenant resource caps in a shared, multi-tenant deployment. No
 * individual request can see another tenant's usage; this guard is the
 * thing that tracks it and enforces isolation regardless.
 */
public class NoisyNeighborGuard {

    private final Map<String, TenantCapPolicy> capsByTenant;
    private final Map<String, Long> cumulativeUnitsByTenant = new ConcurrentHashMap<>();

    public NoisyNeighborGuard(Map<String, TenantCapPolicy> capsByTenant) {
        this.capsByTenant = Map.copyOf(capsByTenant);
    }

    public IsolationDecision evaluate(ResourceUsageRequest request) {
        TenantCapPolicy policy = capsByTenant.get(request.tenantId());
        if (policy == null) {
            throw new IllegalArgumentException(
                    "No cap policy registered for tenant: " + request.tenantId());
        }

        long currentUsage = cumulativeUnitsByTenant.getOrDefault(request.tenantId(), 0L);
        long projectedUsage = currentUsage + request.unitsRequested();

        if (projectedUsage > policy.maxUnitsPerWindow()) {
            return new IsolationDecision(
                    false,
                    request.tenantId(),
                    request.unitsRequested(),
                    currentUsage,
                    policy.maxUnitsPerWindow(),
                    "Tenant " + request.tenantId() + " would exceed its cap of "
                            + policy.maxUnitsPerWindow() + " units (currently at " + currentUsage + ")");
        }

        cumulativeUnitsByTenant.put(request.tenantId(), projectedUsage);
        return new IsolationDecision(
                true,
                request.tenantId(),
                request.unitsRequested(),
                projectedUsage,
                policy.maxUnitsPerWindow(),
                "Within cap");
    }

    public long cumulativeUsageFor(String tenantId) {
        return cumulativeUnitsByTenant.getOrDefault(tenantId, 0L);
    }
}
