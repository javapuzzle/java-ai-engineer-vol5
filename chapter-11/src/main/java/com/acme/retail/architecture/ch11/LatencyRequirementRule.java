package com.acme.retail.architecture.ch11;

/**
 * A requirement mentioning response time or latency implies a real,
 * measurable tail-latency constraint -- the same P99 discipline Chapter
 * 4 established, not just a vague "should be fast."
 */
public class LatencyRequirementRule implements ConstraintDerivationRule {

    @Override
    public boolean appliesTo(Requirement requirement) {
        String text = requirement.description().toLowerCase();
        return text.contains("latency") || text.contains("response time") || text.contains("respond within");
    }

    @Override
    public ArchitecturalConstraint deriveFrom(Requirement requirement) {
        return new ArchitecturalConstraint(
                requirement.id(),
                ConstraintCategory.LATENCY,
                "Must define and meet an explicit P99 latency budget, not just a typical-case average.");
    }
}
