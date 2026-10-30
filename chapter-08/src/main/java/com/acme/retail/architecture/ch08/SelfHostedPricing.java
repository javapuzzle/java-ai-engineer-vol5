package com.acme.retail.architecture.ch08;

import java.math.BigDecimal;

/**
 * A self-hosted deployment's real pricing: a fixed monthly cost --
 * infrastructure, licensing, and operational overhead -- that does not
 * change with request volume.
 */
public record SelfHostedPricing(BigDecimal fixedMonthlyCost) {

    public SelfHostedPricing {
        if (fixedMonthlyCost == null || fixedMonthlyCost.signum() <= 0) {
            throw new IllegalArgumentException("fixedMonthlyCost must be positive");
        }
    }
}
