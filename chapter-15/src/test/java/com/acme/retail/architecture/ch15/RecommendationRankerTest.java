package com.acme.retail.architecture.ch15;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendationRankerTest {

    private final RecommendationRanker ranker = new RecommendationRanker(new FreshnessDecayScorer());
    private final Instant now = Instant.parse("2026-01-01T12:00:00Z");
    private final Duration oneHourHalfLife = Duration.ofHours(1);

    @Test
    void aFresherLessRelevantItemCanOutrankAnOlderMoreRelevantOne() {
        // relevance 1.0, but one full half-life old -> decayed to 0.5
        CandidateItem olderMoreRelevant = new CandidateItem("older", 1.0, now.minus(oneHourHalfLife));
        // relevance 0.6, brand new -> not decayed at all
        CandidateItem newerLessRelevant = new CandidateItem("newer", 0.6, now);

        List<CandidateItem> ranked = ranker.rank(List.of(olderMoreRelevant, newerLessRelevant), now, oneHourHalfLife);

        assertThat(ranked).extracting(CandidateItem::itemId).containsExactly("newer", "older");
    }

    @Test
    void twoEquallyFreshItemsRankByRawRelevance() {
        CandidateItem lowerRelevance = new CandidateItem("low", 0.3, now);
        CandidateItem higherRelevance = new CandidateItem("high", 0.9, now);

        List<CandidateItem> ranked = ranker.rank(List.of(lowerRelevance, higherRelevance), now, oneHourHalfLife);

        assertThat(ranked).extracting(CandidateItem::itemId).containsExactly("high", "low");
    }

    @Test
    void anEmptyCandidateListRanksToAnEmptyList() {
        List<CandidateItem> ranked = ranker.rank(List.of(), now, oneHourHalfLife);

        assertThat(ranked).isEmpty();
    }

    @Test
    void aVeryStaleItemRanksBelowAFreshItemEvenWithMuchHigherRawRelevance() {
        CandidateItem veryStale = new CandidateItem("stale", 5.0, now.minus(oneHourHalfLife.multipliedBy(10)));
        CandidateItem fresh = new CandidateItem("fresh", 0.1, now);

        List<CandidateItem> ranked = ranker.rank(List.of(veryStale, fresh), now, oneHourHalfLife);

        assertThat(ranked).extracting(CandidateItem::itemId).containsExactly("fresh", "stale");
    }
}
