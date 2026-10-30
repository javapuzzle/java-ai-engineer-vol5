package com.acme.retail.architecture.ch20;

/**
 * Where a region's traffic real fails over to during an outage --
 * Chapters 9 and 10's territory. A backup region equal to the primary
 * region is a legitimate, real configuration (same-region redundancy,
 * no cross-border movement at all).
 */
public record RegionFailoverPolicy(String primaryRegion, String backupRegion) {

    public RegionFailoverPolicy {
        if (primaryRegion == null || primaryRegion.isBlank()) {
            throw new IllegalArgumentException("primaryRegion must not be blank");
        }
        if (backupRegion == null || backupRegion.isBlank()) {
            throw new IllegalArgumentException("backupRegion must not be blank");
        }
    }
}
