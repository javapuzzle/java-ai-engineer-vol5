package com.acme.retail.architecture.ch09;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RedundantDependencyPoolTest {

    @Test
    void returnsThePrimaryResultWhenThePrimarySucceeds() {
        Supplier<String> primary = () -> "primary-response";
        Supplier<String> secondary = () -> "secondary-response";
        RedundantDependencyPool pool = new RedundantDependencyPool(List.of(primary, secondary));

        assertThat(pool.callWithRedundancy()).isEqualTo("primary-response");
    }

    @Test
    void fallsBackToTheSecondaryWhenThePrimaryFails() {
        Supplier<String> failingPrimary = () -> {
            throw new RuntimeException("primary is down");
        };
        Supplier<String> secondary = () -> "secondary-response";
        RedundantDependencyPool pool = new RedundantDependencyPool(List.of(failingPrimary, secondary));

        assertThat(pool.callWithRedundancy()).isEqualTo("secondary-response");
    }

    @Test
    void triesEveryDependencyBeforeGivingUp() {
        Supplier<String> failingPrimary = () -> {
            throw new RuntimeException("primary is down");
        };
        Supplier<String> failingSecondary = () -> {
            throw new RuntimeException("secondary is down");
        };
        Supplier<String> tertiary = () -> "tertiary-response";
        RedundantDependencyPool pool = new RedundantDependencyPool(
                List.of(failingPrimary, failingSecondary, tertiary));

        assertThat(pool.callWithRedundancy()).isEqualTo("tertiary-response");
    }

    @Test
    void throwsWhenEveryRedundantDependencyFails() {
        Supplier<String> failingPrimary = () -> {
            throw new RuntimeException("primary is down");
        };
        Supplier<String> failingSecondary = () -> {
            throw new RuntimeException("secondary is down");
        };
        RedundantDependencyPool pool = new RedundantDependencyPool(List.of(failingPrimary, failingSecondary));

        assertThatThrownBy(pool::callWithRedundancy)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("All redundant dependencies failed");
    }

    @Test
    void rejectsAnEmptyPool() {
        assertThatThrownBy(() -> new RedundantDependencyPool(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
