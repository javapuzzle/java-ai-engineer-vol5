package com.acme.retail.architecture.ch04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A real, growing distribution of observed latencies, in milliseconds.
 * Computes both the average and any requested percentile so the two can
 * be compared directly against the same underlying data -- the average
 * alone can look perfectly healthy while the tail does not.
 */
public class LatencyDistribution {

    private final List<Long> samplesMillis = new ArrayList<>();

    public void record(long durationMillis) {
        if (durationMillis < 0) {
            throw new IllegalArgumentException("durationMillis must not be negative");
        }
        samplesMillis.add(durationMillis);
    }

    public int sampleCount() {
        return samplesMillis.size();
    }

    public double average() {
        if (samplesMillis.isEmpty()) {
            throw new IllegalStateException("Cannot compute average of an empty distribution");
        }
        return samplesMillis.stream().mapToLong(Long::longValue).average().orElseThrow();
    }

    /**
     * The nearest-rank percentile of the recorded samples. {@code percentile(50)}
     * is P50 (the median); {@code percentile(99)} is P99.
     */
    public long percentile(double percentile) {
        if (samplesMillis.isEmpty()) {
            throw new IllegalStateException("Cannot compute a percentile of an empty distribution");
        }
        if (percentile <= 0 || percentile > 100) {
            throw new IllegalArgumentException("percentile must be in (0, 100]");
        }

        List<Long> sorted = new ArrayList<>(samplesMillis);
        Collections.sort(sorted);

        int rank = (int) Math.ceil((percentile / 100.0) * sorted.size());
        int index = Math.min(Math.max(rank, 1), sorted.size()) - 1;
        return sorted.get(index);
    }
}
