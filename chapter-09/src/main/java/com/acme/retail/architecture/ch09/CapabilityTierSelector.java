package com.acme.retail.architecture.ch09;

/**
 * Selects a real {@link CapabilityTier} from a real health score --
 * generalizing Volume 4 Chapter 1's statistical health signal from a
 * binary healthy/unhealthy verdict into three explicit levels of
 * service.
 */
public class CapabilityTierSelector {

    static final double FULL_CAPABILITY_THRESHOLD = 0.95;
    static final double DEGRADED_CAPABILITY_THRESHOLD = 0.5;

    public CapabilityTier select(double healthScore) {
        if (healthScore < 0.0 || healthScore > 1.0) {
            throw new IllegalArgumentException("healthScore must be between 0.0 and 1.0");
        }
        if (healthScore >= FULL_CAPABILITY_THRESHOLD) {
            return CapabilityTier.FULL_CAPABILITY;
        }
        if (healthScore >= DEGRADED_CAPABILITY_THRESHOLD) {
            return CapabilityTier.DEGRADED_CAPABILITY;
        }
        return CapabilityTier.SAFE_FALLBACK;
    }
}
