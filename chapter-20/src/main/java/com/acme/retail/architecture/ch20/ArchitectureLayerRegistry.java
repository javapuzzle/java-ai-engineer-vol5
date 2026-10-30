package com.acme.retail.architecture.ch20;

import java.util.LinkedHashMap;
import java.util.Map;

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
