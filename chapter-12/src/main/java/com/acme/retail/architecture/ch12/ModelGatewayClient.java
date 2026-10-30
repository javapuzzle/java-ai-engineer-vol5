package com.acme.retail.architecture.ch12;

public class ModelGatewayClient {

    private final ModelDeploymentTarget target;

    public ModelGatewayClient(ModelDeploymentTarget target) {
        this.target = target;
    }

    public ModelInvocationResult invoke(ModelInvocationRequest request) {
        return target.invoke(request);
    }
}
