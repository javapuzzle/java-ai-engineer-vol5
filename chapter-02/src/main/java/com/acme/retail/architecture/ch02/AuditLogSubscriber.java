package com.acme.retail.architecture.ch02;

import java.util.ArrayList;
import java.util.List;

/**
 * Records every action event for audit purposes. Knows nothing about any
 * other subscriber.
 */
public class AuditLogSubscriber {

    private final List<OpsAgentActionTakenEvent> auditedEvents = new ArrayList<>();

    public void onActionTaken(OpsAgentActionTakenEvent event) {
        auditedEvents.add(event);
    }

    public List<OpsAgentActionTakenEvent> auditedEvents() {
        return List.copyOf(auditedEvents);
    }
}
