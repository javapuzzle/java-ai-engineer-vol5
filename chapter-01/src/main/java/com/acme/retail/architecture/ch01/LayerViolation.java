package com.acme.retail.architecture.ch01;

/**
 * One layering rule violation found by {@link ArchitectureLayerValidator}.
 */
public record LayerViolation(
        String fromComponent,
        ArchitectureLayer fromLayer,
        String toComponent,
        ArchitectureLayer toLayer,
        String reason) {
}
