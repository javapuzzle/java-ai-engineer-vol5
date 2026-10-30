package com.acme.retail.architecture.ch01;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks a set of declared dependency edges against a registry's layering
 * rules and reports every violation found. This is the mechanism that
 * catches "core logic depends back on the guardrail layer" the moment it
 * is declared, instead of relying on a reviewer noticing it in a diagram.
 */
public class ArchitectureLayerValidator {

    public List<LayerViolation> validate(
            ArchitectureLayerRegistry registry, List<DependencyEdge> edges) {

        List<LayerViolation> violations = new ArrayList<>();

        for (DependencyEdge edge : edges) {
            ArchitectureLayer fromLayer = registry.layerOf(edge.fromComponent());
            ArchitectureLayer toLayer = registry.layerOf(edge.toComponent());

            if (!fromLayer.canDependOn(toLayer)) {
                violations.add(new LayerViolation(
                        edge.fromComponent(),
                        fromLayer,
                        edge.toComponent(),
                        toLayer,
                        fromLayer + " may not depend on " + toLayer));
            }
        }

        return violations;
    }
}
