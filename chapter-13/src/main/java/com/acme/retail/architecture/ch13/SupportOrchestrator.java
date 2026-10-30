package com.acme.retail.architecture.ch13;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.List;

/**
 * Composes real multi-agent orchestration with real observability:
 * every request is routed to the specialist that actually handles its
 * topic (falling back to the general specialist otherwise), and every
 * routing decision is measured -- a real Micrometer counter and timer,
 * tagged by which specialist actually handled it -- from the start, not
 * added on afterward.
 */
public class SupportOrchestrator {

    private final List<SpecialistAgent> specialists;
    private final SpecialistAgent generalFallback;
    private final MeterRegistry meterRegistry;

    public SupportOrchestrator(
            List<SpecialistAgent> specialists, SpecialistAgent generalFallback, MeterRegistry meterRegistry) {
        this.specialists = List.copyOf(specialists);
        this.generalFallback = generalFallback;
        this.meterRegistry = meterRegistry;
    }

    public SupportResponse route(SupportRequest request) {
        SpecialistAgent specialist = specialists.stream()
                .filter(candidate -> candidate.handles() == request.topic())
                .findFirst()
                .orElse(generalFallback);

        String specialistName = specialist.getClass().getSimpleName();

        Timer.Sample sample = Timer.start(meterRegistry);
        SupportResponse response = specialist.respond(request);
        sample.stop(meterRegistry.timer("support.request.duration", "specialist", specialistName));

        meterRegistry.counter("support.request.count", "specialist", specialistName).increment();

        return response;
    }
}
