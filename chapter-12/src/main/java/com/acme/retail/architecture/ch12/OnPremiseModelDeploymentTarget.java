package com.acme.retail.architecture.ch12;

public class OnPremiseModelDeploymentTarget implements ModelDeploymentTarget {

    private final String dataCenterId;

    public OnPremiseModelDeploymentTarget(String dataCenterId) {
        if (dataCenterId == null || dataCenterId.isBlank()) {
            throw new IllegalArgumentException("dataCenterId must not be blank");
        }
        this.dataCenterId = dataCenterId;
    }

    @Override
    public ModelInvocationResult invoke(ModelInvocationRequest request) {
        return new ModelInvocationResult("[on-premise response] " + request.prompt(), locationName());
    }

    @Override
    public String locationName() {
        return "ON_PREMISE:" + dataCenterId;
    }
}
