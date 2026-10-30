package com.acme.retail.architecture.ch08;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Computes the real breakeven request volume between a managed API and a
 * self-hosted deployment, and recommends whichever option is actually
 * cheaper for a given projected volume -- derived directly from the two
 * pricing inputs, never assumed as a default.
 */
public class DeploymentCostCalculator {

    public BigDecimal managedMonthlyCost(ManagedApiPricing pricing, long requestsPerMonth) {
        requireNonNegativeVolume(requestsPerMonth);
        return pricing.costPerRequest().multiply(BigDecimal.valueOf(requestsPerMonth));
    }

    public BigDecimal selfHostedMonthlyCost(SelfHostedPricing pricing, long requestsPerMonth) {
        requireNonNegativeVolume(requestsPerMonth);
        return pricing.fixedMonthlyCost();
    }

    /**
     * The smallest request volume at which the self-hosted fixed cost
     * becomes less than or equal to the managed API's variable cost --
     * computed by dividing the fixed cost by the per-request cost and
     * rounding up, since any volume below that point still favors the
     * managed API.
     */
    public long breakEvenRequestsPerMonth(ManagedApiPricing managed, SelfHostedPricing selfHosted) {
        BigDecimal breakEven = selfHosted.fixedMonthlyCost()
                .divide(managed.costPerRequest(), 0, RoundingMode.CEILING);
        return breakEven.longValueExact();
    }

    public DeploymentRecommendation recommend(
            ManagedApiPricing managed, SelfHostedPricing selfHosted, long projectedRequestsPerMonth) {

        BigDecimal managedCost = managedMonthlyCost(managed, projectedRequestsPerMonth);
        BigDecimal selfHostedCost = selfHostedMonthlyCost(selfHosted, projectedRequestsPerMonth);
        long breakEven = breakEvenRequestsPerMonth(managed, selfHosted);

        DeploymentOption recommended = managedCost.compareTo(selfHostedCost) <= 0
                ? DeploymentOption.MANAGED_API
                : DeploymentOption.SELF_HOSTED;

        return new DeploymentRecommendation(recommended, managedCost, selfHostedCost, breakEven);
    }

    private void requireNonNegativeVolume(long requestsPerMonth) {
        if (requestsPerMonth < 0) {
            throw new IllegalArgumentException("requestsPerMonth must not be negative");
        }
    }
}
