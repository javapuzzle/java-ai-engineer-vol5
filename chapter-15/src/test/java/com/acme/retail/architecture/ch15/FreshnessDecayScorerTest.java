package com.acme.retail.architecture.ch15;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class FreshnessDecayScorerTest {

    private final FreshnessDecayScorer scorer = new FreshnessDecayScorer();
    private final Instant now = Instant.parse("2026-01-01T12:00:00Z");
    private final Duration oneHourHalfLife = Duration.ofHours(1);

    @Test
    void aBrandNewItemIsNotDecayedAtAll() {
        CandidateItem item = new CandidateItem("item-1", 0.8, now);

        assertThat(scorer.decayedScore(item, now, oneHourHalfLife)).isCloseTo(0.8, within(0.0001));
    }

    @Test
    void anItemExactlyOneHalfLifeOldIsDecayedByExactlyHalf() {
        CandidateItem item = new CandidateItem("item-2", 1.0, now.minus(oneHourHalfLife));

        assertThat(scorer.decayedScore(item, now, oneHourHalfLife)).isCloseTo(0.5, within(0.0001));
    }

    @Test
    void anItemTwoHalfLivesOldIsDecayedToOneQuarter() {
        CandidateItem item = new CandidateItem("item-3", 1.0, now.minus(oneHourHalfLife.multipliedBy(2)));

        assertThat(scorer.decayedScore(item, now, oneHourHalfLife)).isCloseTo(0.25, within(0.0001));
    }

    @Test
    void rejectsAPublishedAtInTheFuture() {
        CandidateItem item = new CandidateItem("item-4", 0.5, now.plusSeconds(60));

        assertThatThrownBy(() -> scorer.decayedScore(item, now, oneHourHalfLife))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsANonPositiveHalfLife() {
        CandidateItem item = new CandidateItem("item-5", 0.5, now);

        assertThatThrownBy(() -> scorer.decayedScore(item, now, Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
