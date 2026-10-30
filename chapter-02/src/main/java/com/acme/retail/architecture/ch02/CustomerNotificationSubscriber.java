package com.acme.retail.architecture.ch02;

import java.util.ArrayList;
import java.util.List;

/**
 * Sends a customer-facing notification whenever the ops agent takes a
 * real action on their order. Knows nothing about any other subscriber.
 */
public class CustomerNotificationSubscriber {

    private final List<String> sentNotifications = new ArrayList<>();

    public void onActionTaken(OpsAgentActionTakenEvent event) {
        sentNotifications.add(
                "Order " + event.orderId() + ": " + event.actionType() + " was completed.");
    }

    public List<String> sentNotifications() {
        return List.copyOf(sentNotifications);
    }
}
