package com.acme.retail.architecture.ch03;

import java.util.Map;

/**
 * A producer's real, current schema -- the actual fields it emits today,
 * as opposed to whatever a consumer's contract assumes it emits.
 */
public record ProducerSchema(String producerName, Map<String, FieldType> actualFields) {

    public ProducerSchema {
        if (producerName == null || producerName.isBlank()) {
            throw new IllegalArgumentException("producerName must not be blank");
        }
        if (actualFields == null) {
            throw new IllegalArgumentException("actualFields must not be null");
        }
        actualFields = Map.copyOf(actualFields);
    }
}
