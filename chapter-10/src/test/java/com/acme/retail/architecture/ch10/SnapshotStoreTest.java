package com.acme.retail.architecture.ch10;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SnapshotStoreTest {

    private final Instant t0 = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void findsTheMostRecentSnapshotBeforeTheGivenInstant() {
        SnapshotStore store = new SnapshotStore();
        store.save(new SystemStateSnapshot("snap-1", t0, Map.of()));
        store.save(new SystemStateSnapshot("snap-2", t0.plusSeconds(3600), Map.of()));
        store.save(new SystemStateSnapshot("snap-3", t0.plusSeconds(7200), Map.of()));

        SystemStateSnapshot latest = store.latestBefore(t0.plusSeconds(5000));

        assertThat(latest.snapshotId()).isEqualTo("snap-2");
    }

    @Test
    void neverReturnsASnapshotTakenAfterTheGivenInstant() {
        SnapshotStore store = new SnapshotStore();
        store.save(new SystemStateSnapshot("snap-1", t0, Map.of()));
        store.save(new SystemStateSnapshot("snap-future", t0.plusSeconds(100_000), Map.of()));

        SystemStateSnapshot latest = store.latestBefore(t0.plusSeconds(10));

        assertThat(latest.snapshotId()).isEqualTo("snap-1");
    }

    @Test
    void throwsWhenNoSnapshotExistsBeforeTheGivenInstant() {
        SnapshotStore store = new SnapshotStore();
        store.save(new SystemStateSnapshot("snap-1", t0.plusSeconds(1000), Map.of()));

        assertThatThrownBy(() -> store.latestBefore(t0))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void anExactlyMatchingTimestampCounts() {
        SnapshotStore store = new SnapshotStore();
        store.save(new SystemStateSnapshot("snap-1", t0, Map.of()));

        SystemStateSnapshot latest = store.latestBefore(t0);

        assertThat(latest.snapshotId()).isEqualTo("snap-1");
    }
}
