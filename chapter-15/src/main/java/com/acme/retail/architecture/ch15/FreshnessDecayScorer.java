package com.acme.retail.architecture.ch15;

import java.time.Duration;
import java.time.Instant;

/**
 * A real exponential decay function: a candidate's relevance score is
 * multiplied by a decay factor that halves for every full half-life of
 * age. This is the genuine tradeoff a recommendation engine has to make
 * explicit -- an item's raw relevance and its real age are two
 * different numbers, and this is the honest function combining them
 * into one.
 */
public class FreshnessDecayScorer {

    public double decayedScore(CandidateItem item, Instant now, Duration halfLife) {
        if (halfLife == null || halfLife.isZero() || halfLife.isNegative()) {
            throw new IllegalArgumentException("halfLife must be positive");
        }
        if (now.isBefore(item.publishedAt())) {
            throw new IllegalArgumentException("now must not be before the item's publishedAt");
        }

        long ageMillis = Duration.between(item.publishedAt(), now).toMillis();
        double ageRatio = ageMillis / (double) halfLife.toMillis();
        double decayFactor = Math.pow(0.5, ageRatio);

        return item.relevanceScore() * decayFactor;
    }
}
