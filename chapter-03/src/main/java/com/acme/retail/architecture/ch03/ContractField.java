package com.acme.retail.architecture.ch03;

/**
 * One field a consumer declares it depends on: its name, its expected
 * type, and whether the consumer can tolerate the field being absent.
 */
public record ContractField(String name, FieldType type, boolean required) {

    public ContractField {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Contract field name must not be blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Contract field type must not be null");
        }
    }
}
