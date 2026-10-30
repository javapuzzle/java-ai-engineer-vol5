package com.acme.retail.architecture.ch13;

public record SupportRequest(String requestId, SupportTopic topic, String message) {

    public SupportRequest {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("requestId must not be blank");
        }
        if (topic == null) {
            throw new IllegalArgumentException("topic must not be null");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
    }
}
