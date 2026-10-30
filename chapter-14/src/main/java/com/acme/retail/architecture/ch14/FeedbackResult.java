package com.acme.retail.architecture.ch14;

/**
 * The result of reconciling one scoring decision against its real,
 * later-known outcome: was it a false positive (blocked a legitimate
 * transaction) or a false negative (approved an actually fraudulent
 * one) -- two distinct error types with genuinely different real costs.
 */
public record FeedbackResult(String transactionId, boolean falsePositive, boolean falseNegative) {
}
