package com.acme.retail.architecture.ch13;

public class BillingSpecialistAgent implements SpecialistAgent {

    @Override
    public SupportTopic handles() {
        return SupportTopic.BILLING;
    }

    @Override
    public SupportResponse respond(SupportRequest request) {
        return new SupportResponse(getClass().getSimpleName(),
                "Billing specialist: reviewing your charge for \"" + request.message() + "\".");
    }
}
