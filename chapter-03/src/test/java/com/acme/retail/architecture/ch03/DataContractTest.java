package com.acme.retail.architecture.ch03;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DataContractTest {

    @Test
    void rejectsABlankProducerName() {
        assertThatThrownBy(() -> new DataContract("  ", "OpsAgent",
                List.of(new ContractField("orderId", FieldType.STRING, true))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsAContractWithNoFields() {
        assertThatThrownBy(() -> new DataContract("OrderService", "OpsAgent", List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one field");
    }

    @Test
    void rejectsABlankFieldName() {
        assertThatThrownBy(() -> new ContractField("  ", FieldType.STRING, true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
