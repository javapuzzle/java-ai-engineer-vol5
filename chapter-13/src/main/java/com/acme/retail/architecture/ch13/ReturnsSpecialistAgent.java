package com.acme.retail.architecture.ch13;

public class ReturnsSpecialistAgent implements SpecialistAgent {

    @Override
    public SupportTopic handles() {
        return SupportTopic.RETURNS;
    }

    @Override
    public SupportResponse respond(SupportRequest request) {
        return new SupportResponse(getClass().getSimpleName(),
                "Returns specialist: processing your return for \"" + request.message() + "\".");
    }
}
