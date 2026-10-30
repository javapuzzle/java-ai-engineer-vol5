package com.acme.retail.architecture.ch20;

public record TargetArchitectureRequest(String tenantId, String region, long resourceUnits) {

    public TargetArchitectureRequest {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId must not be blank");
        }
        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException("region must not be blank");
        }
        if (resourceUnits <= 0) {
            throw new IllegalArgumentException("resourceUnits must be positive");
        }
    }
}
