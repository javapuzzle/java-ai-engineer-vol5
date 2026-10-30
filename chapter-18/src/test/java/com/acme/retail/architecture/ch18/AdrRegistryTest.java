package com.acme.retail.architecture.ch18;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdrRegistryTest {

    private final Instant t0 = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void currentVersionReturnsTheLatestRecordedVersion() {
        AdrRegistry registry = new AdrRegistry();
        registry.record(new ArchitectureDecisionRecord(
                "ADR-001", 1, "Use event-driven architecture", "Adopt an event bus",
                "Decouples producers from consumers", DecisionStatus.ACCEPTED, t0));
        registry.record(new ArchitectureDecisionRecord(
                "ADR-001", 2, "Use event-driven architecture", "Adopt a durable broker",
                "In-process bus lost events on restart", DecisionStatus.ACCEPTED, t0.plusSeconds(86_400)));

        ArchitectureDecisionRecord current = registry.currentVersion("ADR-001");

        assertThat(current.version()).isEqualTo(2);
        assertThat(current.decision()).isEqualTo("Adopt a durable broker");
    }

    @Test
    void versionAsOfReturnsTheVersionThatWasActuallyInForceAtAGivenTime() {
        AdrRegistry registry = new AdrRegistry();
        registry.record(new ArchitectureDecisionRecord(
                "ADR-001", 1, "Use event-driven architecture", "Adopt an event bus",
                "Decouples producers from consumers", DecisionStatus.ACCEPTED, t0));
        registry.record(new ArchitectureDecisionRecord(
                "ADR-001", 2, "Use event-driven architecture", "Adopt a durable broker",
                "In-process bus lost events on restart", DecisionStatus.ACCEPTED, t0.plusSeconds(86_400)));

        ArchitectureDecisionRecord asOfDayZero = registry.versionAsOf("ADR-001", t0.plusSeconds(3600));

        assertThat(asOfDayZero.version()).isEqualTo(1);
        assertThat(asOfDayZero.decision()).isEqualTo("Adopt an event bus");
    }

    @Test
    void historyReturnsEveryVersionInOrder() {
        AdrRegistry registry = new AdrRegistry();
        registry.record(new ArchitectureDecisionRecord(
                "ADR-002", 1, "Sharding strategy", "Shard by category",
                "Matches query patterns", DecisionStatus.ACCEPTED, t0));
        registry.record(new ArchitectureDecisionRecord(
                "ADR-002", 2, "Sharding strategy", "Shard by category and region",
                "Global expansion needed region-awareness", DecisionStatus.ACCEPTED, t0.plusSeconds(1000)));

        assertThat(registry.history("ADR-002")).hasSize(2);
        assertThat(registry.history("ADR-002").get(0).version()).isEqualTo(1);
        assertThat(registry.history("ADR-002").get(1).version()).isEqualTo(2);
    }

    @Test
    void rejectsAVersionThatSkipsAhead() {
        AdrRegistry registry = new AdrRegistry();
        registry.record(new ArchitectureDecisionRecord(
                "ADR-003", 1, "Title", "Decision", "Rationale", DecisionStatus.ACCEPTED, t0));

        assertThatThrownBy(() -> registry.record(new ArchitectureDecisionRecord(
                "ADR-003", 3, "Title", "New decision", "New rationale", DecisionStatus.ACCEPTED, t0.plusSeconds(1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected version 2");
    }

    @Test
    void throwsWhenQueryingAnUnknownDecisionId() {
        AdrRegistry registry = new AdrRegistry();

        assertThatThrownBy(() -> registry.currentVersion("ADR-UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ADR-UNKNOWN");
    }

    @Test
    void throwsWhenNoVersionExistedYetAtTheGivenInstant() {
        AdrRegistry registry = new AdrRegistry();
        registry.record(new ArchitectureDecisionRecord(
                "ADR-004", 1, "Title", "Decision", "Rationale", DecisionStatus.ACCEPTED, t0));

        assertThatThrownBy(() -> registry.versionAsOf("ADR-004", t0.minusSeconds(1)))
                .isInstanceOf(IllegalStateException.class);
    }
}
