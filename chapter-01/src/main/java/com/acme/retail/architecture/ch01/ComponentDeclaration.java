package com.acme.retail.architecture.ch01;

/**
 * One named component in the architecture and the layer it belongs to.
 */
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
