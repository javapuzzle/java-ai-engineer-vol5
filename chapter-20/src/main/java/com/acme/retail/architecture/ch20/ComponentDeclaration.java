package com.acme.retail.architecture.ch20;

public record ComponentDeclaration(String name, ArchitectureLayer layer) {

    public ComponentDeclaration {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Component name must not be blank");
        }
        if (layer == null) {
            throw new IllegalArgumentException("Component layer must not be null");
        }
    }
}
