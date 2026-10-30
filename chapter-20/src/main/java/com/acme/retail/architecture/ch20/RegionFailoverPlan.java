package com.acme.retail.architecture.ch20;

import java.util.Map;

/**
 * The real, complete disaster-recovery failover plan: for every primary
 * region, which region its traffic actually moves to during a real
 * outage.
 */
public record RegionFailoverPlan(Map<String, RegionFailoverPolicy> policyByRegion) {

    public RegionFailoverPlan {
        if (policyByRegion == null || policyByRegion.isEmpty()) {
            throw new IllegalArgumentException("policyByRegion must not be empty");
        }
        policyByRegion = Map.copyOf(policyByRegion);
    }

    public RegionFailoverPolicy policyFor(String region) {
        RegionFailoverPolicy policy = policyByRegion.get(region);
        if (policy == null) {
            throw new IllegalArgumentException("No failover policy registered for region: " + region);
        }
        return policy;
    }
}
