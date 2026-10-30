package com.acme.retail.architecture.ch20;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AdrRegistryTest {

    @Test
    void recordsTheDiscoveredConflictAndItsResolutionAsTwoRealVersions() {
        AdrRegistry registry = new AdrRegistry();
        Instant beforeReview = Instant.parse("2026-06-01T00:00:00Z");
        Instant afterReview = Instant.parse("2026-06-15T00:00:00Z");

        registry.record(new ArchitectureDecisionRecord(
                "ADR-011",
                1,
                "Regional disaster-recovery failover targets",
                "eu-frankfurt fails over to us-east",
                "us-east has the most spare capacity of any backup region",
                DecisionStatus.ACCEPTED,
                beforeReview));

        registry.record(new ArchitectureDecisionRecord(
                "ADR-011",
                2,
                "Regional disaster-recovery failover targets",
                "eu-frankfurt fails over to eu-dublin",
                "Target architecture review found the us-east failover violated "
                        + "EU-resident tenants' data-residency requirement; eu-dublin keeps "
                        + "failover inside the same jurisdiction",
                DecisionStatus.ACCEPTED,
                afterReview));

        ArchitectureDecisionRecord current = registry.currentVersion("ADR-011");
        ArchitectureDecisionRecord asOfBeforeReview = registry.versionAsOf("ADR-011", beforeReview.plusSeconds(1));

        assertThat(current.version()).isEqualTo(2);
        assertThat(current.decision()).contains("eu-dublin");
        assertThat(asOfBeforeReview.version()).isEqualTo(1);
        assertThat(asOfBeforeReview.decision()).contains("us-east");
    }
}
