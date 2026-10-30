package com.acme.retail.architecture.ch15;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * Ranks real-time candidates by their decayed score, not their raw
 * relevance -- the mechanism that actually enforces the tradeoff a
 * recommendation engine promises: a genuinely more relevant item that
 * arrived too long ago can lose to a fresher, less relevant one.
 */
public class RecommendationRanker {

    private final FreshnessDecayScorer decayScorer;

    public RecommendationRanker(FreshnessDecayScorer decayScorer) {
        this.decayScorer = decayScorer;
    }

    public List<CandidateItem> rank(List<CandidateItem> candidates, Instant now, Duration halfLife) {
        return candidates.stream()
                .sorted(Comparator.comparingDouble(
                        (CandidateItem item) -> decayScorer.decayedScore(item, now, halfLife))
                        .reversed())
                .toList();
    }
}
