package com.acme.retail.architecture.ch16;

/**
 * One real dimension a build-vs-buy decision is scored against, and how
 * much that dimension should actually count toward the final decision.
 * Generalizes Chapter 8's cost-only breakeven comparison into any
 * number of real, independently weighted criteria.
 */
public record ScoringCriterion(String name, double weight) {

    public ScoringCriterion {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (weight <= 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("weight must be in (0, 1]");
        }
    }
}
