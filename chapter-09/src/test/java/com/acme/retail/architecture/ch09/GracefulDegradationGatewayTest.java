package com.acme.retail.architecture.ch09;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GracefulDegradationGatewayTest {

    private final GracefulDegradationGateway gateway = new GracefulDegradationGateway();

    @Test
    void fullCapabilityReturnsTheRealAnswerUnmodified() {
        TierResponse response = gateway.respond(0.99, "Your order ships tomorrow.", "We're experiencing issues.");

        assertThat(response.tier()).isEqualTo(CapabilityTier.FULL_CAPABILITY);
        assertThat(response.responseText()).isEqualTo("Your order ships tomorrow.");
        assertThat(response.usedFallbackData()).isFalse();
    }

    @Test
    void degradedCapabilityStillReturnsRealDataClearlyMarked() {
        TierResponse response = gateway.respond(0.7, "Your order ships tomorrow.", "We're experiencing issues.");

        assertThat(response.tier()).isEqualTo(CapabilityTier.DEGRADED_CAPABILITY);
        assertThat(response.responseText()).isEqualTo("[degraded] Your order ships tomorrow.");
        assertThat(response.usedFallbackData()).isFalse();
    }

    @Test
    void safeFallbackReturnsOnlyTheStaticFallbackAnswer() {
        TierResponse response = gateway.respond(0.1, "Your order ships tomorrow.", "We're experiencing issues.");

        assertThat(response.tier()).isEqualTo(CapabilityTier.SAFE_FALLBACK);
        assertThat(response.responseText()).isEqualTo("We're experiencing issues.");
        assertThat(response.usedFallbackData()).isTrue();
    }
}
