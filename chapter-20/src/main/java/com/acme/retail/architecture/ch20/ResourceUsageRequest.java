package com.acme.retail.architecture.ch20;

public record ResourceUsageRequest(String tenantId, long unitsRequested) {

    public ResourceUsageRequest {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId must not be blank");
        }
        if (unitsRequested <= 0) {
            throw new IllegalArgumentException("unitsRequested must be positive");
        }
    }
}
