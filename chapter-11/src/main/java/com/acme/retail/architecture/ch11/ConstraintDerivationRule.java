package com.acme.retail.architecture.ch11;

/**
 * A real, checkable rule: does this requirement actually imply a
 * specific architectural constraint, and if so, what is it. Each rule
 * is deliberately narrow and explicit -- there is no inference beyond
 * what the rule itself states.
 */
public interface ConstraintDerivationRule {

    boolean appliesTo(Requirement requirement);

    ArchitecturalConstraint deriveFrom(Requirement requirement);
}
