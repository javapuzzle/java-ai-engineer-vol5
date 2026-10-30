package com.acme.retail.architecture.ch10;

import java.time.Instant;
import java.util.Map;

/**
 * A real, point-in-time snapshot of critical AI system state -- the
 * cumulative guardrail counters (Volume 4 Chapter 4's cost guardrail,
 * Chapter 6's tenant caps) that a restored system needs in order to keep
 * enforcing its limits correctly, not just its code and configuration.
 */
public record SystemStateSnapshot(String snapshotId, Instant takenAt, Map<String, Long> guardrailCounters) {

    public SystemStateSnapshot {
        if (snapshotId == null || snapshotId.isBlank()) {
            throw new IllegalArgumentException("snapshotId must not be blank");
        }
        if (takenAt == null) {
            throw new IllegalArgumentException("takenAt must not be null");
        }
        guardrailCounters = Map.copyOf(guardrailCounters);
    }
}
