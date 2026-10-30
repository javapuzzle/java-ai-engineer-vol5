package com.acme.retail.architecture.ch15;

import java.time.Instant;

/**
 * One candidate for real-time recommendation: its raw, undecayed
 * relevance score, and when it was actually published -- the two real
 * inputs a freshness-decay scorer trades off against each other.
 */
public record CandidateItem(String itemId, double relevanceScore, Instant publishedAt) {

    public CandidateItem {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("itemId must not be blank");
        }
        if (relevanceScore < 0.0) {
            throw new IllegalArgumentException("relevanceScore must not be negative");
        }
        if (publishedAt == null) {
            throw new IllegalArgumentException("publishedAt must not be null");
        }
    }
}
