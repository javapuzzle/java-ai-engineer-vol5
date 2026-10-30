package com.acme.retail.architecture.ch20;

public record LayerViolation(
        String fromComponent,
        ArchitectureLayer fromLayer,
        String toComponent,
        ArchitectureLayer toLayer,
        String reason) {
}
