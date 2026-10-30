package com.acme.retail.architecture.ch13;

public class ShippingSpecialistAgent implements SpecialistAgent {

    @Override
    public SupportTopic handles() {
        return SupportTopic.SHIPPING;
    }

    @Override
    public SupportResponse respond(SupportRequest request) {
        return new SupportResponse(getClass().getSimpleName(),
                "Shipping specialist: tracking your order for \"" + request.message() + "\".");
    }
}
