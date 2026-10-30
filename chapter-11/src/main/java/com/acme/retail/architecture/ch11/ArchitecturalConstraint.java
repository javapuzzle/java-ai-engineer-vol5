package com.acme.retail.architecture.ch11;

/**
 * One concrete architectural constraint, traceable back to the exact
 * requirement that produced it -- the real, checkable link this
 * chapter's framework exists to make explicit.
 */
public record ArchitecturalConstraint(
        String requirementId, ConstraintCategory category, String constraintDescription) {
}
