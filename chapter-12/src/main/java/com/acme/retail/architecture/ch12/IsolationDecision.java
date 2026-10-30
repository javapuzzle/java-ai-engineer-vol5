package com.acme.retail.architecture.ch12;

public record IsolationDecision(
        boolean allowed,
        String tenantId,
        long unitsRequested,
        long cumulativeUnitsUsed,
        long cap,
        String reason) {
}
