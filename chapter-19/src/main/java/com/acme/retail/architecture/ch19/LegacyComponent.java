package com.acme.retail.architecture.ch19;

/**
 * One real legacy component being considered for migration: how tightly
 * coupled it is to the rest of the system (lower is safer to migrate
 * first) and how much real business value migrating it would deliver
 * (higher is more worth doing first).
 */
public record LegacyComponent(String name, int couplingScore, int businessValueScore) {

    public LegacyComponent {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (couplingScore < 1 || couplingScore > 10) {
            throw new IllegalArgumentException("couplingScore must be between 1 and 10");
        }
        if (businessValueScore < 1 || businessValueScore > 10) {
            throw new IllegalArgumentException("businessValueScore must be between 1 and 10");
        }
    }
}
