package com.acme.retail.architecture.ch10;

import java.time.Duration;
import java.time.Instant;

/**
 * Computes real RPO and RTO from an actual snapshot's age and an actual
 * recovery attempt's duration, and checks each against a negotiated
 * objective -- never an assumed number nobody has tested against a real
 * snapshot and a real recovery attempt.
 */
public class DisasterRecoveryEvaluator {

    public RecoveryOutcome evaluate(
            RecoveryObjective objective,
            SystemStateSnapshot restoredSnapshot,
            Instant disasterOccurredAt,
            Instant recoveryStartedAt,
            Instant recoveryCompletedAt) {

        Duration actualRpo = Duration.between(restoredSnapshot.takenAt(), disasterOccurredAt);
        Duration actualRto = Duration.between(recoveryStartedAt, recoveryCompletedAt);

        boolean metRpoTarget = actualRpo.compareTo(objective.targetRpo()) <= 0;
        boolean metRtoTarget = actualRto.compareTo(objective.targetRto()) <= 0;

        return new RecoveryOutcome(actualRpo, actualRto, metRpoTarget, metRtoTarget);
    }
}
