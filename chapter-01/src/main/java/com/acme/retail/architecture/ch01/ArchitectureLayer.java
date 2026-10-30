package com.acme.retail.architecture.ch01;

import java.util.EnumSet;
import java.util.Set;

/**
 * A layer in an AI-native enterprise architecture, ordered top-to-bottom.
 * Each layer explicitly declares which other layers it is allowed to
 * depend on -- this is the enforced rule, not the diagram of it.
 */
public enum ArchitectureLayer {

    PRESENTATION,
    ORCHESTRATION,
    GUARDRAIL,
    DOMAIN,
    MODEL_GATEWAY,
    INFRASTRUCTURE;

    /**
     * Whether a component in this layer may depend on a component in
     * {@code target}. A layer may always depend on itself. Notably,
     * {@link #GUARDRAIL} may depend on {@link #DOMAIN}, but
     * {@link #DOMAIN} may never depend back on {@link #GUARDRAIL} --
     * core logic must never depend on the layer meant to constrain it.
     */
    public boolean canDependOn(ArchitectureLayer target) {
        if (this == target) {
            return true;
        }
        return allowedTargets().contains(target);
    }

    private Set<ArchitectureLayer> allowedTargets() {
        return switch (this) {
            case PRESENTATION -> EnumSet.of(ORCHESTRATION);
            case ORCHESTRATION -> EnumSet.of(GUARDRAIL, DOMAIN);
            case GUARDRAIL -> EnumSet.of(DOMAIN, MODEL_GATEWAY, INFRASTRUCTURE);
            case DOMAIN -> EnumSet.of(MODEL_GATEWAY, INFRASTRUCTURE);
            case MODEL_GATEWAY -> EnumSet.of(INFRASTRUCTURE);
            case INFRASTRUCTURE -> EnumSet.noneOf(ArchitectureLayer.class);
        };
    }
}
