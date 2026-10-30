package com.acme.retail.architecture.ch14;

import java.util.HashMap;
import java.util.Map;

/**
 * A real feedback loop: records what was decided at scoring time, and
 * later reconciles that decision against a real, actual outcome --
 * making the asymmetric cost of a false positive (a blocked legitimate
 * transaction) and a false negative (an approved fraud) two distinct,
 * measurable events instead of one combined "the model was wrong"
 * signal.
 */
public class FraudFeedbackLoop {

    private final Map<String, ScoringRecord> scoringHistory = new HashMap<>();

    public void record(ScoringRecord scoringRecord) {
        scoringHistory.put(scoringRecord.transactionId(), scoringRecord);
    }

    public FeedbackResult reconcile(ActualOutcome outcome) {
        ScoringRecord original = scoringHistory.get(outcome.transactionId());
        if (original == null) {
            throw new IllegalArgumentException(
                    "No scoring record found for transaction: " + outcome.transactionId());
        }

        boolean falsePositive = original.decision() == FraudDecision.BLOCK && !outcome.wasActuallyFraud();
        boolean falseNegative = original.decision() == FraudDecision.APPROVE && outcome.wasActuallyFraud();

        return new FeedbackResult(outcome.transactionId(), falsePositive, falseNegative);
    }
}
