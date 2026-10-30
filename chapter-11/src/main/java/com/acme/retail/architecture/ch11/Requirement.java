package com.acme.retail.architecture.ch11;

/**
 * One requirement as actually stated by a stakeholder -- the raw input
 * a design has to be checked against, before any architectural decision
 * gets made.
 */
public record Requirement(String id, String description) {

    public Requirement {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description must not be blank");
        }
    }
}
