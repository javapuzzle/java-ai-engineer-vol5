package com.acme.retail.architecture.ch20;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The capstone's centerpiece: catches the exact conflict none of
 * Chapters 1-19's individually correct mechanisms would ever surface on
 * their own -- a tenant's real data-residency jurisdiction (Chapter 7),
 * checked directly against the real jurisdiction its region's disaster
 * -recovery failover (Chapters 9-10) would actually move it to.
 * Regional failover works. Residency-aware deployment routing works.
 * Composed, without this check, a real failover event could silently
 * move a residency-bound tenant's traffic across a jurisdiction
 * boundary -- correct on both sides, wrong together.
 */
public class TargetArchitectureValidator {

    private final RegionJurisdictionDirectory jurisdictionDirectory;

    public TargetArchitectureValidator(RegionJurisdictionDirectory jurisdictionDirectory) {
        this.jurisdictionDirectory = jurisdictionDirectory;
    }

    public List<ResidencyConflict> findResidencyFailoverConflicts(
            Map<String, ResidencyRequirement> residencyByTenant,
            Map<String, String> tenantRegionAssignment,
            RegionFailoverPlan failoverPlan) {

        List<ResidencyConflict> conflicts = new ArrayList<>();

        for (ResidencyRequirement requirement : residencyByTenant.values()) {
            String assignedRegion = tenantRegionAssignment.get(requirement.tenantId());
            if (assignedRegion == null) {
                throw new IllegalStateException(
                        "No region assignment found for tenant: " + requirement.tenantId());
            }

            String assignedJurisdiction = jurisdictionDirectory.jurisdictionOf(assignedRegion);
            if (!assignedJurisdiction.equals(requirement.requiredJurisdiction())) {
                throw new IllegalStateException(
                        "Tenant " + requirement.tenantId() + " is assigned to region " + assignedRegion
                                + " (" + assignedJurisdiction + ") but requires jurisdiction "
                                + requirement.requiredJurisdiction());
            }

            RegionFailoverPolicy failoverPolicy = failoverPlan.policyFor(assignedRegion);
            String failoverJurisdiction = jurisdictionDirectory.jurisdictionOf(failoverPolicy.backupRegion());

            if (!failoverJurisdiction.equals(requirement.requiredJurisdiction())) {
                conflicts.add(new ResidencyConflict(
                        requirement.tenantId(),
                        requirement.requiredJurisdiction(),
                        failoverPolicy.backupRegion(),
                        failoverJurisdiction,
                        "Failing over " + assignedRegion + " to " + failoverPolicy.backupRegion()
                                + " (" + failoverJurisdiction + ") would move tenant " + requirement.tenantId()
                                + "'s data outside its required jurisdiction " + requirement.requiredJurisdiction()));
            }
        }

        return conflicts;
    }
}
