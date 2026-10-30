package com.acme.retail.architecture.ch20;

import java.util.EnumSet;
import java.util.Set;

/**
 * Chapter 1's layering model, reused unmodified at the capstone level to
 * validate the target architecture's own components -- the same
 * enforced-rule discipline this volume opened with, now checking the
 * system that closes it.
 */
public enum ArchitectureLayer {

    PRESENTATION,
    ORCHESTRATION,
    GUARDRAIL,
    DOMAIN,
    MODEL_GATEWAY,
    INFRASTRUCTURE;

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
