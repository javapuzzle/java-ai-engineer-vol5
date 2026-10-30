package com.acme.retail.architecture.ch09;

/**
 * The response actually returned for a given tier: which tier was
 * selected, the text returned, and whether that text came from real,
 * live data or from a static, safe fallback.
 */
public record TierResponse(CapabilityTier tier, String responseText, boolean usedFallbackData) {
}
