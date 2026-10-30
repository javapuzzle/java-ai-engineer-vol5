package com.acme.retail.architecture.ch06;

/**
 * The real, enforced cap for one tenant's cumulative resource usage in a
 * shared deployment -- the boundary that keeps one tenant's load from
 * ever degrading another tenant's.
 */
public record TenantCapPolicy(String tenantId, long maxUnitsPerWindow) {

    public TenantCapPolicy {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId must not be blank");
        }
        if (maxUnitsPerWindow <= 0) {
            throw new IllegalArgumentException("maxUnitsPerWindow must be positive");
        }
    }
}
