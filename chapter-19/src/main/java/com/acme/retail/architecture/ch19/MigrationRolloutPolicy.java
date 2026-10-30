package com.acme.retail.architecture.ch19;

/**
 * How much of one component's real traffic should currently be shifted
 * to its new, AI-native replacement -- a real, adjustable percentage,
 * not an all-or-nothing cutover.
 */
public record MigrationRolloutPolicy(String componentName, int rolloutPercentage) {

    public MigrationRolloutPolicy {
        if (componentName == null || componentName.isBlank()) {
            throw new IllegalArgumentException("componentName must not be blank");
        }
        if (rolloutPercentage < 0 || rolloutPercentage > 100) {
            throw new IllegalArgumentException("rolloutPercentage must be between 0 and 100");
        }
    }
}
