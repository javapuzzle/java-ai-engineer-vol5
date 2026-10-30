package com.acme.retail.architecture.ch20;

/**
 * The exact conflict this capstone exists to catch: a tenant's real
 * data-residency jurisdiction, and the real jurisdiction its region's
 * disaster-recovery failover would actually move it to -- two
 * independently correct mechanisms whose composition needed its own
 * boundary.
 */
public record ResidencyConflict(
        String tenantId,
        String requiredJurisdiction,
        String failoverRegion,
        String failoverJurisdiction,
        String reason) {
}
