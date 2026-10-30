package com.acme.retail.architecture.ch07;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModelGatewayClientTest {

    @Test
    void theSameGatewayClientCodeWorksIdenticallyAgainstTheCloudTarget() {
        ModelGatewayClient client = new ModelGatewayClient(new CloudModelDeploymentTarget());

        ModelInvocationResult result = client.invoke(
                new ModelInvocationRequest("tenant-acme", "What is my order status?"));

        assertThat(result.servedByLocation()).isEqualTo("CLOUD");
        assertThat(result.responseText()).contains("What is my order status?");
    }

    @Test
    void theSameGatewayClientCodeWorksIdenticallyAgainstAnOnPremiseTarget() {
        ModelGatewayClient client = new ModelGatewayClient(new OnPremiseModelDeploymentTarget("dc-frankfurt"));

        ModelInvocationResult result = client.invoke(
                new ModelInvocationRequest("tenant-enterprise", "What is my order status?"));

        assertThat(result.servedByLocation()).isEqualTo("ON_PREMISE:dc-frankfurt");
        assertThat(result.responseText()).contains("What is my order status?");
    }

    @Test
    void onlyTheInjectedTargetChangesBetweenTheTwoDeploymentModels() {
        ModelInvocationRequest request = new ModelInvocationRequest("tenant-acme", "Same prompt");

        ModelGatewayClient cloudClient = new ModelGatewayClient(new CloudModelDeploymentTarget());
        ModelGatewayClient onPremiseClient = new ModelGatewayClient(new OnPremiseModelDeploymentTarget("dc-1"));

        ModelInvocationResult cloudResult = cloudClient.invoke(request);
        ModelInvocationResult onPremiseResult = onPremiseClient.invoke(request);

        assertThat(cloudResult.servedByLocation()).isNotEqualTo(onPremiseResult.servedByLocation());
    }
}
