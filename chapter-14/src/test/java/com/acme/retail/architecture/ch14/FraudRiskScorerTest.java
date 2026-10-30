package com.acme.retail.architecture.ch14;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class FraudRiskScorerTest {

    private final FraudRiskScorer scorer = new FraudRiskScorer();

    @Test
    void scoresALowRiskTransactionAtZero() {
        TransactionSignal signal = new TransactionSignal("txn-1", 50.0, false, false, 1);

        assertThat(scorer.score(signal)).isEqualTo(0.0);
    }

    @Test
    void combinesWeightsForEachRealSignalPresent() {
        TransactionSignal signal = new TransactionSignal("txn-2", 1500.0, true, false, 1);

        // amount weight (0.30) + new device weight (0.25) = 0.55
        assertThat(scorer.score(signal)).isCloseTo(0.55, within(0.0001));
    }

    @Test
    void capsTheScoreAtOneEvenWhenEverySignalIsPresent() {
        TransactionSignal signal = new TransactionSignal("txn-3", 1500.0, true, true, 10);

        assertThat(scorer.score(signal)).isEqualTo(1.0);
    }

    @Test
    void rejectsANegativeAmount() {
        assertThatThrownBy(() -> new TransactionSignal("txn-4", -1.0, false, false, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
