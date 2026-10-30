package com.acme.retail.architecture.ch11;

import java.util.ArrayList;
import java.util.List;

/**
 * The framework's core mechanism: derive every constraint a set of
 * stated requirements actually implies, using explicit rules, and
 * report any requirement that no rule recognized at all -- the gap a
 * verbal walkthrough alone would silently skip past.
 */
public class RequirementConstraintMapper {

    private final List<ConstraintDerivationRule> rules;

    public RequirementConstraintMapper(List<ConstraintDerivationRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public List<ArchitecturalConstraint> deriveConstraints(List<Requirement> requirements) {
        List<ArchitecturalConstraint> constraints = new ArrayList<>();
        for (Requirement requirement : requirements) {
            for (ConstraintDerivationRule rule : rules) {
                if (rule.appliesTo(requirement)) {
                    constraints.add(rule.deriveFrom(requirement));
                }
            }
        }
        return constraints;
    }

    public List<Requirement> unaddressedRequirements(List<Requirement> requirements) {
        List<Requirement> unaddressed = new ArrayList<>();
        for (Requirement requirement : requirements) {
            boolean matchedAnyRule = rules.stream().anyMatch(rule -> rule.appliesTo(requirement));
            if (!matchedAnyRule) {
                unaddressed.add(requirement);
            }
        }
        return unaddressed;
    }
}
