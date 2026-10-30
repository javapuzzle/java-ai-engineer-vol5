package com.acme.retail.architecture.ch08;

import java.math.BigDecimal;

/**
 * A managed model API's real pricing: a variable cost charged per
 * request, with no fixed cost regardless of volume.
 */
public record ManagedApiPricing(BigDecimal costPerRequest) {

    public ManagedApiPricing {
        if (costPerRequest == null || costPerRequest.signum() <= 0) {
            throw new IllegalArgumentException("costPerRequest must be positive");
        }
    }
}
