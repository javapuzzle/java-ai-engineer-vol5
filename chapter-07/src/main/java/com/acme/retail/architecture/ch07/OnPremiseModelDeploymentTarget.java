package com.acme.retail.architecture.ch07;

/**
 * Runs the invocation against a model deployed inside a specific
 * customer's own data center -- the target a hard data-residency
 * requirement forces the router to use instead of the cloud.
 */
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
        return new ModelInvocationResult(
                "[on-premise response] " + request.prompt(),
                locationName());
    }

    @Override
    public String locationName() {
        return "ON_PREMISE:" + dataCenterId;
    }
}
