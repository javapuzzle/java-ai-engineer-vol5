package com.acme.retail.architecture.ch02;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class EventBusTest {

    private final EventBus eventBus = new EventBus();

    @Test
    void independentSubscribersEachReceiveThePublishedEventWithoutKnowingAboutEachOther() {
        AuditLogSubscriber auditLog = new AuditLogSubscriber();
        CustomerNotificationSubscriber notifications = new CustomerNotificationSubscriber();
        FinanceLedgerSubscriber ledger = new FinanceLedgerSubscriber();

        eventBus.subscribe(OpsAgentActionTakenEvent.class, auditLog::onActionTaken);
        eventBus.subscribe(OpsAgentActionTakenEvent.class, notifications::onActionTaken);
        eventBus.subscribe(OpsAgentActionTakenEvent.class, ledger::onActionTaken);

        OpsAgentActionTakenEvent event = new OpsAgentActionTakenEvent(
                "evt-1", Instant.now(), "tenant-acme", "REFUND", "order-500");

        eventBus.publish(event);

        assertThat(auditLog.auditedEvents()).containsExactly(event);
        assertThat(notifications.sentNotifications())
                .containsExactly("Order order-500: REFUND was completed.");
        assertThat(ledger.ledgerEntries()).containsExactly(
                new FinanceLedgerSubscriber.LedgerEntry("tenant-acme", "order-500", new BigDecimal("25.00")));
    }

    @Test
    void addingAFourthSubscriberRequiresNoChangeToThePublishCall() {
        AuditLogSubscriber auditLog = new AuditLogSubscriber();
        eventBus.subscribe(OpsAgentActionTakenEvent.class, auditLog::onActionTaken);

        StringBuilder analyticsSink = new StringBuilder();
        eventBus.subscribe(OpsAgentActionTakenEvent.class,
                (OpsAgentActionTakenEvent evt) -> analyticsSink.append(evt.actionType()));

        OpsAgentActionTakenEvent event = new OpsAgentActionTakenEvent(
                "evt-2", Instant.now(), "tenant-acme", "REPLACEMENT_SHIPMENT", "order-501");

        eventBus.publish(event);

        assertThat(auditLog.auditedEvents()).hasSize(1);
        assertThat(analyticsSink.toString()).isEqualTo("REPLACEMENT_SHIPMENT");
        assertThat(eventBus.subscriberCountFor(OpsAgentActionTakenEvent.class)).isEqualTo(2);
    }

    @Test
    void publishingWithNoSubscribersDoesNothingAndDoesNotThrow() {
        OpsAgentActionTakenEvent event = new OpsAgentActionTakenEvent(
                "evt-3", Instant.now(), "tenant-acme", "REFUND", "order-502");

        eventBus.publish(event);

        assertThat(eventBus.subscriberCountFor(OpsAgentActionTakenEvent.class)).isEqualTo(0);
    }
}
