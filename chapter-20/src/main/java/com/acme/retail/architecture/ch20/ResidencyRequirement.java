package com.acme.retail.architecture.ch20;

/**
 * A tenant's real, hard data-residency constraint -- Chapter 7's
 * territory -- expressed as the jurisdiction its data must never leave,
 * not a single literal data center. A jurisdiction (e.g. "EU") can
 * legitimately span more than one real region.
 */
public record ResidencyRequirement(String tenantId, String requiredJurisdiction) {

    public ResidencyRequirement {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId must not be blank");
        }
        if (requiredJurisdiction == null || requiredJurisdiction.isBlank()) {
            throw new IllegalArgumentException("requiredJurisdiction must not be blank");
        }
    }
}
