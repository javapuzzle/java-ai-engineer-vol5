package com.acme.retail.architecture.ch14;

/**
 * The real, observable signals about one transaction that a fraud
 * scorer actually has available at decision time -- deliberately
 * simple, honest inputs, not a black box.
 */
public record TransactionSignal(
        String transactionId, double amount, boolean newDevice, boolean newLocation, int velocityLastHour) {

    public TransactionSignal {
        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("transactionId must not be blank");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        if (velocityLastHour < 0) {
            throw new IllegalArgumentException("velocityLastHour must not be negative");
        }
    }
}
