package com.acme.retail.architecture.ch04;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LatencyDistributionTest {

    @Test
    void averageCanLookHealthyWhileP99RevealsARealTail() {
        LatencyDistribution distribution = new LatencyDistribution();
        for (int i = 0; i < 96; i++) {
            distribution.record(50);
        }
        for (int i = 0; i < 4; i++) {
            distribution.record(1000);
        }

        assertThat(distribution.average()).isEqualTo(88.0);
        assertThat(distribution.percentile(50)).isEqualTo(50L);
        assertThat(distribution.percentile(99)).isEqualTo(1000L);
    }

    @Test
    void percentileUsesNearestRankOnASimpleEvenlySpacedDistribution() {
        LatencyDistribution distribution = new LatencyDistribution();
        List.of(10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L)
                .forEach(distribution::record);

        assertThat(distribution.percentile(50)).isEqualTo(50L);
        assertThat(distribution.percentile(90)).isEqualTo(90L);
        assertThat(distribution.percentile(100)).isEqualTo(100L);
    }

    @Test
    void rejectsANegativeDuration() {
        LatencyDistribution distribution = new LatencyDistribution();
        assertThatThrownBy(() -> distribution.record(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsWhenComputingStatisticsOfAnEmptyDistribution() {
        LatencyDistribution distribution = new LatencyDistribution();
        assertThatThrownBy(distribution::average).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> distribution.percentile(99)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void rejectsAnOutOfRangePercentile() {
        LatencyDistribution distribution = new LatencyDistribution();
        distribution.record(10);
        assertThatThrownBy(() -> distribution.percentile(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> distribution.percentile(101)).isInstanceOf(IllegalArgumentException.class);
    }
}
