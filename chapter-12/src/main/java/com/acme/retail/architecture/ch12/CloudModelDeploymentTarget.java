package com.acme.retail.architecture.ch12;

/**
 * Serves a region's traffic from Acme's managed cloud deployment for
 * that region.
 */
public class CloudModelDeploymentTarget implements ModelDeploymentTarget {

    private final String regionCode;

    public CloudModelDeploymentTarget(String regionCode) {
        if (regionCode == null || regionCode.isBlank()) {
            throw new IllegalArgumentException("regionCode must not be blank");
        }
        this.regionCode = regionCode;
    }

    @Override
    public ModelInvocationResult invoke(ModelInvocationRequest request) {
        return new ModelInvocationResult("[cloud response] " + request.prompt(), locationName());
    }

    @Override
    public String locationName() {
        return "CLOUD:" + regionCode;
    }
}
