package com.acme.retail.architecture.ch14;

/**
 * Maps a real risk score to a real, defined action, via explicit,
 * documented thresholds.
 */
public class FraudDecisionEngine {

    static final double BLOCK_THRESHOLD = 0.7;
    static final double REVIEW_THRESHOLD = 0.4;

    public FraudDecision decide(double riskScore) {
        if (riskScore < 0.0 || riskScore > 1.0) {
            throw new IllegalArgumentException("riskScore must be between 0.0 and 1.0");
        }
        if (riskScore >= BLOCK_THRESHOLD) {
            return FraudDecision.BLOCK;
        }
        if (riskScore >= REVIEW_THRESHOLD) {
            return FraudDecision.REVIEW;
        }
        return FraudDecision.APPROVE;
    }
}
