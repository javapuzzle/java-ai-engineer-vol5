package com.acme.retail.architecture.ch20;

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
