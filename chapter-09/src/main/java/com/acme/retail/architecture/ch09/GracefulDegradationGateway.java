package com.acme.retail.architecture.ch09;

/**
 * Picks a real capability tier from a dependency's real health score,
 * and returns a genuinely different real response for each tier --
 * degraded capability still returns real, live data, clearly marked as
 * degraded; only the safe-fallback tier ever falls back to static data.
 */
public class GracefulDegradationGateway {

    private final CapabilityTierSelector selector = new CapabilityTierSelector();

    public TierResponse respond(double dependencyHealthScore, String realAnswer, String safeFallbackAnswer) {
        CapabilityTier tier = selector.select(dependencyHealthScore);

        return switch (tier) {
            case FULL_CAPABILITY -> new TierResponse(tier, realAnswer, false);
            case DEGRADED_CAPABILITY -> new TierResponse(tier, "[degraded] " + realAnswer, false);
            case SAFE_FALLBACK -> new TierResponse(tier, safeFallbackAnswer, true);
        };
    }
}
