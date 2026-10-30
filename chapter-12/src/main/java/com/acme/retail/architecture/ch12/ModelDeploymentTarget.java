package com.acme.retail.architecture.ch12;

public interface ModelDeploymentTarget {

    ModelInvocationResult invoke(ModelInvocationRequest request);

    String locationName();
}
