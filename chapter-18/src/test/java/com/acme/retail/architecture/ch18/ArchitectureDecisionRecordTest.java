package com.acme.retail.architecture.ch18;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArchitectureDecisionRecordTest {

    @Test
    void rejectsANonPositiveVersion() {
        assertThatThrownBy(() -> new ArchitectureDecisionRecord(
                "ADR-1", 0, "Title", "Decision", "Rationale", DecisionStatus.ACCEPTED, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsABlankDecisionId() {
        assertThatThrownBy(() -> new ArchitectureDecisionRecord(
                " ", 1, "Title", "Decision", "Rationale", DecisionStatus.ACCEPTED, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsABlankRationale() {
        assertThatThrownBy(() -> new ArchitectureDecisionRecord(
                "ADR-1", 1, "Title", "Decision", " ", DecisionStatus.ACCEPTED, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
