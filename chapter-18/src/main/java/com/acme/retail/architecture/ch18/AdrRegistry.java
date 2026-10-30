package com.acme.retail.architecture.ch18;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A real, durable, queryable record of every architecture decision ever
 * made -- and every revision of it -- generalizing Volume 4 Chapter 10's
 * versioned-policy-registry shape so "which rule was in force when"
 * applies to architecture decisions exactly as it did to guardrail
 * policies.
 */
public class AdrRegistry {

    private final Map<String, List<ArchitectureDecisionRecord>> versionsByDecisionId = new HashMap<>();

    public void record(ArchitectureDecisionRecord adr) {
        List<ArchitectureDecisionRecord> history =
                versionsByDecisionId.computeIfAbsent(adr.decisionId(), id -> new ArrayList<>());

        int expectedVersion = history.size() + 1;
        if (adr.version() != expectedVersion) {
            throw new IllegalArgumentException(
                    "Expected version " + expectedVersion + " for decision " + adr.decisionId()
                            + " but got version " + adr.version());
        }

        history.add(adr);
    }

    public ArchitectureDecisionRecord currentVersion(String decisionId) {
        List<ArchitectureDecisionRecord> history = requireHistory(decisionId);
        return history.get(history.size() - 1);
    }

    /**
     * The version of a decision that was actually in force at a given
     * point in time -- the same "which rules were in force when" query
     * Volume 4 Chapter 10 built for guardrail policy, now answering the
     * equivalent question for architecture decisions.
     */
    public ArchitectureDecisionRecord versionAsOf(String decisionId, Instant instant) {
        List<ArchitectureDecisionRecord> history = requireHistory(decisionId);

        Optional<ArchitectureDecisionRecord> mostRecentBefore = history.stream()
                .filter(adr -> !adr.recordedAt().isAfter(instant))
                .max((a, b) -> a.recordedAt().compareTo(b.recordedAt()));

        return mostRecentBefore.orElseThrow(() -> new IllegalStateException(
                "No version of decision " + decisionId + " existed as of " + instant));
    }

    public List<ArchitectureDecisionRecord> history(String decisionId) {
        return List.copyOf(requireHistory(decisionId));
    }

    private List<ArchitectureDecisionRecord> requireHistory(String decisionId) {
        List<ArchitectureDecisionRecord> history = versionsByDecisionId.get(decisionId);
        if (history == null || history.isEmpty()) {
            throw new IllegalArgumentException("No decision record found for id: " + decisionId);
        }
        return history;
    }
}
