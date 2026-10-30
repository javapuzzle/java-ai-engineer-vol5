package com.acme.retail.architecture.ch20;

import java.util.Map;

/**
 * The real, authoritative mapping of which jurisdiction each physical
 * region actually sits in -- the fact both the residency requirement
 * and the failover plan have to be checked against.
 */
public record RegionJurisdictionDirectory(Map<String, String> jurisdictionByRegion) {

    public RegionJurisdictionDirectory {
        if (jurisdictionByRegion == null || jurisdictionByRegion.isEmpty()) {
            throw new IllegalArgumentException("jurisdictionByRegion must not be empty");
        }
        jurisdictionByRegion = Map.copyOf(jurisdictionByRegion);
    }

    public String jurisdictionOf(String region) {
        String jurisdiction = jurisdictionByRegion.get(region);
        if (jurisdiction == null) {
            throw new IllegalArgumentException("No jurisdiction registered for region: " + region);
        }
        return jurisdiction;
    }
}
