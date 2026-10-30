package com.acme.retail.architecture.ch01;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The set of components declared for one architecture, keyed by name.
 * This is the registry a validator checks dependency edges against --
 * "which layer is this component actually in" has to be a lookup, not an
 * assumption.
 */
public class ArchitectureLayerRegistry {

    private final Map<String, ArchitectureLayer> componentsByName = new LinkedHashMap<>();

    public ArchitectureLayerRegistry register(ComponentDeclaration component) {
        componentsByName.put(component.name(), component.layer());
        return this;
    }

    public ArchitectureLayer layerOf(String componentName) {
        ArchitectureLayer layer = componentsByName.get(componentName);
        if (layer == null) {
            throw new IllegalArgumentException(
                    "No component registered with name: " + componentName);
        }
        return layer;
    }

    public boolean isRegistered(String componentName) {
        return componentsByName.containsKey(componentName);
    }
}
