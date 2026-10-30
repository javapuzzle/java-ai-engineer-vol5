package com.acme.retail.architecture.ch08;

import java.math.BigDecimal;

/**
 * A real, computed recommendation for one projected request volume: the
 * monthly cost of each option, and which one is actually cheaper --
 * never a guess, always derived from the two real pricing inputs.
 */
public record DeploymentRecommendation(
        DeploymentOption recommended,
        BigDecimal managedMonthlyCost,
        BigDecimal selfHostedMonthlyCost,
        long breakEvenRequestsPerMonth) {
}
