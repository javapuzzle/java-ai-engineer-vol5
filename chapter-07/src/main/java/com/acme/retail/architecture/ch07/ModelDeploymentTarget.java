package com.acme.retail.architecture.ch07;

/**
 * The Strategy interface: something that can actually run a model
 * invocation, regardless of where it physically executes. Calling code
 * depends only on this interface, never on a specific deployment target.
 */
public interface ModelDeploymentTarget {

    ModelInvocationResult invoke(ModelInvocationRequest request);

    String locationName();
}
