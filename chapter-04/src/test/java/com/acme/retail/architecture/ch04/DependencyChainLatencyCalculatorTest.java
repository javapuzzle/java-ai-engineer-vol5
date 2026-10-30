package com.acme.retail.architecture.ch04;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DependencyChainLatencyCalculatorTest {

    private final DependencyChainLatencyCalculator calculator = new DependencyChainLatencyCalculator();

    @Test
    void sequentialTotalIsTheSumOfEveryStage() {
        List<Long> stages = List.of(50L, 60L, 45L, 500L, 55L);

        assertThat(calculator.sequentialTotalMillis(stages)).isEqualTo(710L);
    }

    @Test
    void parallelTotalIsBoundedByTheSingleSlowestStage() {
        List<Long> stages = List.of(50L, 60L, 45L, 500L, 55L);

        assertThat(calculator.parallelTotalMillis(stages)).isEqualTo(500L);
    }

    @Test
    void parallelizingTheSameStagesReducesTheTotalDramatically() {
        List<Long> stages = List.of(50L, 60L, 45L, 500L, 55L);

        long sequential = calculator.sequentialTotalMillis(stages);
        long parallel = calculator.parallelTotalMillis(stages);

        assertThat(parallel).isLessThan(sequential);
        assertThat(sequential - parallel).isEqualTo(210L);
    }

    @Test
    void rejectsAnEmptyChain() {
        assertThatThrownBy(() -> calculator.sequentialTotalMillis(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> calculator.parallelTotalMillis(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
