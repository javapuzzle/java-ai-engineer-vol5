package com.acme.retail.architecture.ch11;

/**
 * A requirement about data never leaving a region implies a real
 * hybrid-deployment constraint -- Chapter 7's territory -- not a
 * compliance checkbox with no architectural consequence.
 */
public class DataResidencyRequirementRule implements ConstraintDerivationRule {

    @Override
    public boolean appliesTo(Requirement requirement) {
        String text = requirement.description().toLowerCase();
        return text.contains("residency") || text.contains("never leave") || text.contains("data center");
    }

    @Override
    public ArchitecturalConstraint deriveFrom(Requirement requirement) {
        return new ArchitecturalConstraint(
                requirement.id(),
                ConstraintCategory.DATA_RESIDENCY,
                "Must route this tenant to an on-premise or region-pinned deployment target, not a shared default.");
    }
}
