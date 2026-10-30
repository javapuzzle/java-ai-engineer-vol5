package com.acme.retail.architecture.ch02;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OpsAgentActionTakenEventTest {

    @Test
    void rejectsBlankTenantId() {
        assertThatThrownBy(() -> new OpsAgentActionTakenEvent(
                "evt-1", Instant.now(), " ", "REFUND", "order-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tenantId");
    }

    @Test
    void rejectsNullOccurredAt() {
        assertThatThrownBy(() -> new OpsAgentActionTakenEvent(
                "evt-1", null, "tenant-acme", "REFUND", "order-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("occurredAt");
    }

    @Test
    void rejectsBlankOrderId() {
        assertThatThrownBy(() -> new OpsAgentActionTakenEvent(
                "evt-1", Instant.now(), "tenant-acme", "REFUND", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("orderId");
    }
}
