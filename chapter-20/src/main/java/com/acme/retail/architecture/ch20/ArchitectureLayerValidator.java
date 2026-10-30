package com.acme.retail.architecture.ch20;

import java.util.ArrayList;
import java.util.List;

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
