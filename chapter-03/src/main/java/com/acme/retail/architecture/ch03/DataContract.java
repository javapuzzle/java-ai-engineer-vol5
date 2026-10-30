package com.acme.retail.architecture.ch03;

import java.util.List;

/**
 * A real, explicit agreement between one data producer and one AI
 * consumer: exactly which fields the consumer depends on, and what type
 * each one is expected to be. This is the thing a schema change should be
 * checked against before it ships -- not an assumption nobody wrote down.
 */
public record DataContract(String producerName, String consumerName, List<ContractField> fields) {

    public DataContract {
        if (producerName == null || producerName.isBlank()) {
            throw new IllegalArgumentException("producerName must not be blank");
        }
        if (consumerName == null || consumerName.isBlank()) {
            throw new IllegalArgumentException("consumerName must not be blank");
        }
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("A data contract must declare at least one field");
        }
        fields = List.copyOf(fields);
    }
}
