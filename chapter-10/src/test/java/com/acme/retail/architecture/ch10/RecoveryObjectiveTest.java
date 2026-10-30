package com.acme.retail.architecture.ch10;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecoveryObjectiveTest {

    @Test
    void rejectsANegativeTargetRpo() {
        assertThatThrownBy(() -> new RecoveryObjective(Duration.ofMinutes(-1), Duration.ofHours(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsANegativeTargetRto() {
        assertThatThrownBy(() -> new RecoveryObjective(Duration.ofMinutes(30), Duration.ofHours(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void snapshotRejectsABlankId() {
        assertThatThrownBy(() -> new SystemStateSnapshot(" ", Instant.now(), java.util.Map.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
