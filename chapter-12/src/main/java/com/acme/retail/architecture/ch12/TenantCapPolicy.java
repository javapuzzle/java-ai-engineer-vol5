package com.acme.retail.architecture.ch12;

/**
 * A tenant's enforced cap in this global platform -- Chapter 6's
 * isolation shape, reused here unmodified.
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
