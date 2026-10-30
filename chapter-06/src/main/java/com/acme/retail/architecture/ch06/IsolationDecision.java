package com.acme.retail.architecture.ch06;

/**
 * The outcome of checking one tenant's resource request against their
 * cap: whether it's allowed, and if not, exactly why -- the same
 * allowed/blocked-with-reason shape as Volume 3's {@code RefundDecision}
 * and Volume 4's {@code BudgetDecision}.
 */
public record IsolationDecision(
        boolean allowed,
        String tenantId,
        long unitsRequested,
        long cumulativeUnitsUsed,
        long cap,
        String reason) {
}
