package com.acme.retail.architecture.ch05;

/**
 * One retrievable document, tagged with the domain category a shard
 * router partitions on -- deliberately a meaningful business key
 * (a support-document category), not an arbitrary document ID.
 */
public record IndexedDocument(String id, String category, String content) {

    public IndexedDocument {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category must not be blank");
        }
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
    }
}
