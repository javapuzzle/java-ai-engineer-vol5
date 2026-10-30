package com.acme.retail.architecture.ch07;

/**
 * Runs the invocation against Acme's managed cloud model deployment.
 */
public class CloudModelDeploymentTarget implements ModelDeploymentTarget {

    @Override
    public ModelInvocationResult invoke(ModelInvocationRequest request) {
        return new ModelInvocationResult(
                "[cloud response] " + request.prompt(),
                locationName());
    }

    @Override
    public String locationName() {
        return "CLOUD";
    }
}
