package com.acme.retail.architecture.ch13;

/**
 * One specialist agent: declares the exact topic it handles, and
 * produces a real response for a request on that topic -- the
 * multi-agent orchestration shape this series established in Volume 3,
 * reused here as the routing target for this case study's orchestrator.
 */
public interface SpecialistAgent {

    SupportTopic handles();

    SupportResponse respond(SupportRequest request);
}
