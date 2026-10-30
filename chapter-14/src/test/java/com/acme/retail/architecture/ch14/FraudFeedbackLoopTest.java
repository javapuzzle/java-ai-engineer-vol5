package com.acme.retail.architecture.ch14;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FraudFeedbackLoopTest {

    private final FraudFeedbackLoop feedbackLoop = new FraudFeedbackLoop();

    @Test
    void reportsAFalsePositiveWhenABlockedTransactionTurnsOutToBeLegitimate() {
        feedbackLoop.record(new ScoringRecord("txn-1", 0.8, FraudDecision.BLOCK));

        FeedbackResult result = feedbackLoop.reconcile(new ActualOutcome("txn-1", false));

        assertThat(result.falsePositive()).isTrue();
        assertThat(result.falseNegative()).isFalse();
    }

    @Test
    void reportsAFalseNegativeWhenAnApprovedTransactionTurnsOutToBeFraud() {
        feedbackLoop.record(new ScoringRecord("txn-2", 0.1, FraudDecision.APPROVE));

        FeedbackResult result = feedbackLoop.reconcile(new ActualOutcome("txn-2", true));

        assertThat(result.falsePositive()).isFalse();
        assertThat(result.falseNegative()).isTrue();
    }

    @Test
    void aCorrectBlockOfActualFraudIsNeitherFalsePositiveNorFalseNegative() {
        feedbackLoop.record(new ScoringRecord("txn-3", 0.9, FraudDecision.BLOCK));

        FeedbackResult result = feedbackLoop.reconcile(new ActualOutcome("txn-3", true));

        assertThat(result.falsePositive()).isFalse();
        assertThat(result.falseNegative()).isFalse();
    }

    @Test
    void aCorrectApprovalOfALegitimateTransactionIsNeitherFalsePositiveNorFalseNegative() {
        feedbackLoop.record(new ScoringRecord("txn-4", 0.1, FraudDecision.APPROVE));

        FeedbackResult result = feedbackLoop.reconcile(new ActualOutcome("txn-4", false));

        assertThat(result.falsePositive()).isFalse();
        assertThat(result.falseNegative()).isFalse();
    }

    @Test
    void aReviewedTransactionIsNeverClassifiedAsFalsePositiveOrFalseNegative() {
        feedbackLoop.record(new ScoringRecord("txn-5", 0.5, FraudDecision.REVIEW));

        FeedbackResult fraudOutcome = feedbackLoop.reconcile(new ActualOutcome("txn-5", true));

        assertThat(fraudOutcome.falsePositive()).isFalse();
        assertThat(fraudOutcome.falseNegative()).isFalse();
    }

    @Test
    void throwsWhenReconcilingAnOutcomeWithNoMatchingScoringRecord() {
        assertThatThrownBy(() -> feedbackLoop.reconcile(new ActualOutcome("txn-unknown", true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("txn-unknown");
    }
}
