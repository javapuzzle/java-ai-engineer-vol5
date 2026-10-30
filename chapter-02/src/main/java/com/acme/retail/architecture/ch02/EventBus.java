package com.acme.retail.architecture.ch02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A real, minimal event bus: publishers publish a {@link DomainEvent}
 * without knowing who, if anyone, is listening; subscribers register for
 * a specific event type without knowing who else has done the same.
 * Adding a new subscriber never requires touching the publisher.
 */
public class EventBus {

    private final Map<Class<?>, List<Consumer<DomainEvent>>> subscribersByType = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> void subscribe(Class<T> eventType, Consumer<T> handler) {
        subscribersByType
                .computeIfAbsent(eventType, key -> new ArrayList<>())
                .add((Consumer<DomainEvent>) handler);
    }

    public void publish(DomainEvent event) {
        List<Consumer<DomainEvent>> subscribers = subscribersByType.get(event.getClass());
        if (subscribers == null) {
            return;
        }
        for (Consumer<DomainEvent> subscriber : subscribers) {
            subscriber.accept(event);
        }
    }

    public int subscriberCountFor(Class<?> eventType) {
        List<Consumer<DomainEvent>> subscribers = subscribersByType.get(eventType);
        return subscribers == null ? 0 : subscribers.size();
    }
}
