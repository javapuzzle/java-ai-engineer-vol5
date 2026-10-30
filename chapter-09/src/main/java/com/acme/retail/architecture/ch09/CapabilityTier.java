package com.acme.retail.architecture.ch09;

/**
 * A real level of service, defined explicitly instead of collapsing
 * every non-perfect state into "down."
 */
public enum CapabilityTier {
    FULL_CAPABILITY,
    DEGRADED_CAPABILITY,
    SAFE_FALLBACK
}
