package com.acme.retail.architecture.ch02;

import java.time.Instant;

/**
 * Published once the ops agent actually takes a real, consequential
 * action -- the kind of event several unrelated downstream systems need
 * to know about without any of them being wired into the code that took
 * the action.
 */
public record OpsAgentActionTakenEvent(
        String eventId,
        Instant occurredAt,
        String tenantId,
        String actionType,
        String orderId) implements DomainEvent {

    public OpsAgentActionTakenEvent {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("eventId must not be blank");
        }
        if (occurredAt == null) {
            throw new IllegalArgumentException("occurredAt must not be null");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId must not be blank");
        }
        if (actionType == null || actionType.isBlank()) {
            throw new IllegalArgumentException("actionType must not be blank");
        }
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId must not be blank");
        }
    }
}
