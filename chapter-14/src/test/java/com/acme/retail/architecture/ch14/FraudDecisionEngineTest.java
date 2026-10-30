package com.acme.retail.architecture.ch14;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FraudDecisionEngineTest {

    private final FraudDecisionEngine engine = new FraudDecisionEngine();

    @Test
    void aLowRiskScoreIsApproved() {
        assertThat(engine.decide(0.1)).isEqualTo(FraudDecision.APPROVE);
    }

    @Test
    void aMidRangeRiskScoreIsSentToReview() {
        assertThat(engine.decide(0.55)).isEqualTo(FraudDecision.REVIEW);
        assertThat(engine.decide(0.4)).isEqualTo(FraudDecision.REVIEW);
    }

    @Test
    void aHighRiskScoreIsBlocked() {
        assertThat(engine.decide(0.7)).isEqualTo(FraudDecision.BLOCK);
        assertThat(engine.decide(1.0)).isEqualTo(FraudDecision.BLOCK);
    }

    @Test
    void rejectsAnOutOfRangeScore() {
        assertThatThrownBy(() -> engine.decide(-0.1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> engine.decide(1.1)).isInstanceOf(IllegalArgumentException.class);
    }
}
