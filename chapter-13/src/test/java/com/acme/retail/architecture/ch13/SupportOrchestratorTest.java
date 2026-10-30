package com.acme.retail.architecture.ch13;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SupportOrchestratorTest {

    private SupportOrchestrator orchestrator(SimpleMeterRegistry registry) {
        return new SupportOrchestrator(
                List.of(new BillingSpecialistAgent(), new ShippingSpecialistAgent(), new ReturnsSpecialistAgent()),
                new GeneralSpecialistAgent(),
                registry);
    }

    @Test
    void routesABillingRequestToTheBillingSpecialist() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        SupportOrchestrator orchestrator = orchestrator(registry);

        SupportResponse response = orchestrator.route(
                new SupportRequest("req-1", SupportTopic.BILLING, "Why was I charged twice?"));

        assertThat(response.specialistName()).isEqualTo("BillingSpecialistAgent");
        assertThat(response.responseText()).contains("Billing specialist");
    }

    @Test
    void anUnmatchedTopicFallsBackToTheGeneralSpecialist() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        SupportOrchestrator orchestrator = orchestrator(registry);

        SupportResponse response = orchestrator.route(
                new SupportRequest("req-2", SupportTopic.GENERAL, "I have a general question."));

        assertThat(response.specialistName()).isEqualTo("GeneralSpecialistAgent");
    }

    @Test
    void recordsARealCounterTaggedByWhichSpecialistHandledTheRequest() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        SupportOrchestrator orchestrator = orchestrator(registry);

        orchestrator.route(new SupportRequest("req-1", SupportTopic.BILLING, "Charge question"));
        orchestrator.route(new SupportRequest("req-2", SupportTopic.BILLING, "Another charge question"));
        orchestrator.route(new SupportRequest("req-3", SupportTopic.SHIPPING, "Where is my order?"));

        double billingCount = registry.get("support.request.count")
                .tag("specialist", "BillingSpecialistAgent")
                .counter()
                .count();
        double shippingCount = registry.get("support.request.count")
                .tag("specialist", "ShippingSpecialistAgent")
                .counter()
                .count();

        assertThat(billingCount).isEqualTo(2.0);
        assertThat(shippingCount).isEqualTo(1.0);
    }

    @Test
    void recordsARealTimerForEverySpecialistThatHandlesARequest() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        SupportOrchestrator orchestrator = orchestrator(registry);

        orchestrator.route(new SupportRequest("req-1", SupportTopic.RETURNS, "I want to return this."));

        long returnsTimerCount = registry.get("support.request.duration")
                .tag("specialist", "ReturnsSpecialistAgent")
                .timer()
                .count();

        assertThat(returnsTimerCount).isEqualTo(1L);
    }

    @Test
    void differentSpecialistsAccumulateIndependentMetrics() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        SupportOrchestrator orchestrator = orchestrator(registry);

        orchestrator.route(new SupportRequest("req-1", SupportTopic.BILLING, "Charge question"));
        orchestrator.route(new SupportRequest("req-2", SupportTopic.RETURNS, "Return question"));

        double billingCount = registry.get("support.request.count")
                .tag("specialist", "BillingSpecialistAgent")
                .counter()
                .count();
        double returnsCount = registry.get("support.request.count")
                .tag("specialist", "ReturnsSpecialistAgent")
                .counter()
                .count();

        assertThat(billingCount).isEqualTo(1.0);
        assertThat(returnsCount).isEqualTo(1.0);
    }
}
