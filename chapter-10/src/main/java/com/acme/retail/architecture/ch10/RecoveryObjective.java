package com.acme.retail.architecture.ch10;

import java.time.Duration;

/**
 * The real, negotiated targets for how much data an AI system may lose
 * (RPO) and how long it may take to recover (RTO) -- a plan without
 * these numbers has nothing to actually test a real recovery against.
 */
public record RecoveryObjective(Duration targetRpo, Duration targetRto) {

    public RecoveryObjective {
        if (targetRpo == null || targetRpo.isNegative()) {
            throw new IllegalArgumentException("targetRpo must not be null or negative");
        }
        if (targetRto == null || targetRto.isNegative()) {
            throw new IllegalArgumentException("targetRto must not be null or negative");
        }
    }
}
