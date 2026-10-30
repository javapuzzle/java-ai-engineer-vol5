package com.acme.retail.architecture.ch12;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Chapter 6's per-tenant isolation guardrail, reused here unmodified to
 * keep one tenant's global platform usage from ever affecting another's.
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
