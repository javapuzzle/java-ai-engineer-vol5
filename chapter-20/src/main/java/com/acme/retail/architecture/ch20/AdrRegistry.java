package com.acme.retail.architecture.ch20;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Chapter 18's versioned ADR registry, reused unmodified at the
 * capstone level to document the exact conflict this chapter discovers
 * and its resolution -- a real decision record, not a memory of what
 * the team decided.
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
