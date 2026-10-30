package com.acme.retail.architecture.ch01;

/**
 * A declared "fromComponent calls toComponent" dependency, independent of
 * which layer either component happens to live in.
 */
public record DependencyEdge(String fromComponent, String toComponent) {

    public DependencyEdge {
        if (fromComponent == null || fromComponent.isBlank()) {
            throw new IllegalArgumentException("fromComponent must not be blank");
        }
        if (toComponent == null || toComponent.isBlank()) {
            throw new IllegalArgumentException("toComponent must not be blank");
        }
    }
}
