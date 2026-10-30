package com.acme.retail.architecture.ch13;

/**
 * The fallback specialist for any topic no other registered specialist
 * handles -- every request gets a real response, never a dead end.
 */
public class GeneralSpecialistAgent implements SpecialistAgent {

    @Override
    public SupportTopic handles() {
        return SupportTopic.GENERAL;
    }

    @Override
    public SupportResponse respond(SupportRequest request) {
        return new SupportResponse(getClass().getSimpleName(),
                "General specialist: looking into \"" + request.message() + "\".");
    }
}
