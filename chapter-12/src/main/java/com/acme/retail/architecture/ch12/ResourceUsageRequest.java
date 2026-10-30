package com.acme.retail.architecture.ch12;

/**
 * One tenant's request to consume a real amount of shared resource in
 * this global platform -- copied from Chapter 6's isolation shape into
 * this case study's own package, per this series' module-independence
 * convention.
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
