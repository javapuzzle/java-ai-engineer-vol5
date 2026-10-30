package com.acme.retail.architecture.ch02;

import java.time.Instant;

/**
 * A real domain event: something that happened, independent of who, if
 * anyone, ends up reacting to it.
 */
public interface DomainEvent {

    String eventId();

    Instant occurredAt();
}
