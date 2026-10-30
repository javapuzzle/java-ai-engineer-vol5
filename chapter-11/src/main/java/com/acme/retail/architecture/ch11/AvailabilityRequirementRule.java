package com.acme.retail.architecture.ch11;

/**
 * A requirement mentioning uptime or availability implies a real
 * redundancy and graceful-degradation constraint -- Chapter 9's
 * territory, not just a percentage written in a document.
 */
public class AvailabilityRequirementRule implements ConstraintDerivationRule {

    @Override
    public boolean appliesTo(Requirement requirement) {
        String text = requirement.description().toLowerCase();
        return text.contains("available") || text.contains("uptime") || text.contains("downtime");
    }

    @Override
    public ArchitecturalConstraint deriveFrom(Requirement requirement) {
        return new ArchitecturalConstraint(
                requirement.id(),
                ConstraintCategory.AVAILABILITY,
                "Must design real redundancy and graceful-degradation tiers, not assume infrastructure alone provides this.");
    }
}
