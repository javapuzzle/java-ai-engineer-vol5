package com.acme.retail.architecture.ch06;

/**
 * One tenant's request to consume a real amount of shared resource
 * (compute units for a model call, in this chapter's story) inside a
 * shared, multi-tenant deployment.
 */
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
