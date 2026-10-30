package com.acme.retail.architecture.ch10;

import java.time.Duration;

/**
 * The real, measured result of one recovery attempt: how much data was
 * actually lost, how long recovery actually took, and whether each of
 * those real numbers met the negotiated objective.
 */
public record RecoveryOutcome(
        Duration actualRpo,
        Duration actualRto,
        boolean metRpoTarget,
        boolean metRtoTarget) {

    public boolean metBothTargets() {
        return metRpoTarget && metRtoTarget;
    }
}
