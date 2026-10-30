package com.acme.retail.architecture.ch12;

public record ModelInvocationRequest(String tenantId, String prompt) {

    public ModelInvocationRequest {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId must not be blank");
        }
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("prompt must not be blank");
        }
    }
}
