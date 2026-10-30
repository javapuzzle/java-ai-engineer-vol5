package com.acme.retail.architecture.ch10;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A real store of state snapshots, ordered by when they were actually
 * taken. Recovery needs the most recent snapshot that existed
 * <em>before</em> a disaster occurred -- not simply the newest snapshot
 * on record, which could postdate the disaster and not exist in a real
 * recovery scenario at all.
 */
public class SnapshotStore {

    private final List<SystemStateSnapshot> snapshots = new ArrayList<>();

    public void save(SystemStateSnapshot snapshot) {
        snapshots.add(snapshot);
    }

    public SystemStateSnapshot latestBefore(Instant instant) {
        return findLatestBefore(instant)
                .orElseThrow(() -> new IllegalStateException(
                        "No snapshot exists before " + instant));
    }

    private Optional<SystemStateSnapshot> findLatestBefore(Instant instant) {
        return snapshots.stream()
                .filter(snapshot -> !snapshot.takenAt().isAfter(instant))
                .max((a, b) -> a.takenAt().compareTo(b.takenAt()));
    }

    public int snapshotCount() {
        return snapshots.size();
    }
}
