package com.acme.retail.architecture.ch07;

/**
 * The single piece of calling code every chapter's ops-agent workflow
 * would actually use. It depends only on {@link ModelDeploymentTarget},
 * never on a concrete cloud or on-premise implementation -- this class
 * itself never changes no matter which target it's constructed with.
 */
public class ModelGatewayClient {

    private final ModelDeploymentTarget target;

    public ModelGatewayClient(ModelDeploymentTarget target) {
        this.target = target;
    }

    public ModelInvocationResult invoke(ModelInvocationRequest request) {
        return target.invoke(request);
    }
}
