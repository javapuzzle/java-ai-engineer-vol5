package com.acme.retail.architecture.ch18;

import java.time.Instant;

/**
 * One real, versioned architecture decision -- generalizing Volume 4
 * Chapter 10's versioned guardrail-policy shape from "which policy was
 * in force" to "which architectural decision was in force," at a
 * specific point in time.
 */
public record ArchitectureDecisionRecord(
        String decisionId,
        int version,
        String title,
        String decision,
        String rationale,
        DecisionStatus status,
        Instant recordedAt) {

    public ArchitectureDecisionRecord {
        if (decisionId == null || decisionId.isBlank()) {
            throw new IllegalArgumentException("decisionId must not be blank");
        }
        if (version <= 0) {
            throw new IllegalArgumentException("version must be positive");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        if (decision == null || decision.isBlank()) {
            throw new IllegalArgumentException("decision must not be blank");
        }
        if (rationale == null || rationale.isBlank()) {
            throw new IllegalArgumentException("rationale must not be blank");
        }
        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        if (recordedAt == null) {
            throw new IllegalArgumentException("recordedAt must not be null");
        }
    }
}
