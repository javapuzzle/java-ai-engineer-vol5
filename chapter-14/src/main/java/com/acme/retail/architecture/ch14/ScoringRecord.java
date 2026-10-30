package com.acme.retail.architecture.ch14;

/**
 * What was actually decided for one transaction at scoring time --
 * recorded so a later, real outcome can be reconciled against it.
 */
public record ScoringRecord(String transactionId, double riskScore, FraudDecision decision) {
}
