package com.acme.retail.architecture.ch10;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DisasterRecoveryEvaluatorTest {

    private final DisasterRecoveryEvaluator evaluator = new DisasterRecoveryEvaluator();
    private final Instant snapshotTakenAt = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void computesActualRpoFromTheRealSnapshotAgeAndMeetsATarget() {
        RecoveryObjective objective = new RecoveryObjective(Duration.ofMinutes(30), Duration.ofHours(4));
        SystemStateSnapshot snapshot = new SystemStateSnapshot("snap-1", snapshotTakenAt, Map.of());
        Instant disasterOccurredAt = snapshotTakenAt.plusSeconds(15 * 60);
        Instant recoveryStartedAt = disasterOccurredAt;
        Instant recoveryCompletedAt = recoveryStartedAt.plus(Duration.ofHours(1));

        RecoveryOutcome outcome = evaluator.evaluate(
                objective, snapshot, disasterOccurredAt, recoveryStartedAt, recoveryCompletedAt);

        assertThat(outcome.actualRpo()).isEqualTo(Duration.ofMinutes(15));
        assertThat(outcome.metRpoTarget()).isTrue();
        assertThat(outcome.actualRto()).isEqualTo(Duration.ofHours(1));
        assertThat(outcome.metRtoTarget()).isTrue();
        assertThat(outcome.metBothTargets()).isTrue();
    }

    @Test
    void reportsAMissedRpoTargetWhenTheSnapshotIsOlderThanTheDisasterAllows() {
        RecoveryObjective objective = new RecoveryObjective(Duration.ofMinutes(30), Duration.ofHours(4));
        SystemStateSnapshot snapshot = new SystemStateSnapshot("snap-1", snapshotTakenAt, Map.of());
        Instant disasterOccurredAt = snapshotTakenAt.plusSeconds(45 * 60);
        Instant recoveryStartedAt = disasterOccurredAt;
        Instant recoveryCompletedAt = recoveryStartedAt.plus(Duration.ofHours(1));

        RecoveryOutcome outcome = evaluator.evaluate(
                objective, snapshot, disasterOccurredAt, recoveryStartedAt, recoveryCompletedAt);

        assertThat(outcome.actualRpo()).isEqualTo(Duration.ofMinutes(45));
        assertThat(outcome.metRpoTarget()).isFalse();
        assertThat(outcome.metBothTargets()).isFalse();
    }

    @Test
    void reportsAMissedRtoTargetWhenRecoveryTakesLongerThanAllowed() {
        RecoveryObjective objective = new RecoveryObjective(Duration.ofMinutes(30), Duration.ofHours(1));
        SystemStateSnapshot snapshot = new SystemStateSnapshot("snap-1", snapshotTakenAt, Map.of());
        Instant disasterOccurredAt = snapshotTakenAt.plusSeconds(10 * 60);
        Instant recoveryStartedAt = disasterOccurredAt;
        Instant recoveryCompletedAt = recoveryStartedAt.plus(Duration.ofHours(2));

        RecoveryOutcome outcome = evaluator.evaluate(
                objective, snapshot, disasterOccurredAt, recoveryStartedAt, recoveryCompletedAt);

        assertThat(outcome.metRpoTarget()).isTrue();
        assertThat(outcome.actualRto()).isEqualTo(Duration.ofHours(2));
        assertThat(outcome.metRtoTarget()).isFalse();
        assertThat(outcome.metBothTargets()).isFalse();
    }

    @Test
    void anActualDurationExactlyEqualToTheTargetCounts() {
        RecoveryObjective objective = new RecoveryObjective(Duration.ofMinutes(30), Duration.ofHours(1));
        SystemStateSnapshot snapshot = new SystemStateSnapshot("snap-1", snapshotTakenAt, Map.of());
        Instant disasterOccurredAt = snapshotTakenAt.plus(Duration.ofMinutes(30));
        Instant recoveryStartedAt = disasterOccurredAt;
        Instant recoveryCompletedAt = recoveryStartedAt.plus(Duration.ofHours(1));

        RecoveryOutcome outcome = evaluator.evaluate(
                objective, snapshot, disasterOccurredAt, recoveryStartedAt, recoveryCompletedAt);

        assertThat(outcome.metBothTargets()).isTrue();
    }
}
