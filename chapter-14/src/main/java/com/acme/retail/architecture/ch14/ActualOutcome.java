package com.acme.retail.architecture.ch14;

/**
 * What actually happened with a transaction, discovered later --
 * a chargeback, a confirmed legitimate purchase, or a confirmed fraud
 * report -- the real ground truth a scoring decision is checked
 * against.
 */
public record ActualOutcome(String transactionId, boolean wasActuallyFraud) {
}
