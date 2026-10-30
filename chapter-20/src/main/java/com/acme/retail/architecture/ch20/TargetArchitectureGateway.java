package com.acme.retail.architecture.ch20;

import java.util.List;
import java.util.Map;

/**
 * The one composed entry point for the target architecture: tenant
 * isolation (Chapter 6) runs first, cheapest and most decisive check
 * first, exactly as Chapter 12 established -- then, only for a tenant
 * with a real residency requirement, the new residency-vs-failover
 * check runs. Neither individual mechanism needed to change to be
 * composed this way; only this gateway, and the validator it calls,
 * are new.
 */
public class TargetArchitectureGateway {

    private final NoisyNeighborGuard tenantGuard;
    private final TargetArchitectureValidator validator;
    private final Map<String, ResidencyRequirement> residencyByTenant;
    private final Map<String, String> tenantRegionAssignment;
    private final RegionFailoverPlan failoverPlan;

    public TargetArchitectureGateway(
            NoisyNeighborGuard tenantGuard,
            TargetArchitectureValidator validator,
            Map<String, ResidencyRequirement> residencyByTenant,
            Map<String, String> tenantRegionAssignment,
            RegionFailoverPlan failoverPlan) {
        this.tenantGuard = tenantGuard;
        this.validator = validator;
        this.residencyByTenant = Map.copyOf(residencyByTenant);
        this.tenantRegionAssignment = Map.copyOf(tenantRegionAssignment);
        this.failoverPlan = failoverPlan;
    }

    public TargetArchitectureDecision evaluate(TargetArchitectureRequest request) {
        IsolationDecision isolation = tenantGuard.evaluate(
                new ResourceUsageRequest(request.tenantId(), request.resourceUnits()));

        if (!isolation.allowed()) {
            return new TargetArchitectureDecision(false, isolation.reason(), List.of());
        }

        if (!residencyByTenant.containsKey(request.tenantId())) {
            return new TargetArchitectureDecision(true, "OK - no residency requirement", List.of());
        }

        List<ResidencyConflict> allConflicts = validator.findResidencyFailoverConflicts(
                residencyByTenant, tenantRegionAssignment, failoverPlan);

        List<ResidencyConflict> tenantConflicts = allConflicts.stream()
                .filter(conflict -> conflict.tenantId().equals(request.tenantId()))
                .toList();

        if (!tenantConflicts.isEmpty()) {
            return new TargetArchitectureDecision(
                    false, "Residency/failover conflict detected", tenantConflicts);
        }

        return new TargetArchitectureDecision(true, "OK", List.of());
    }
}
