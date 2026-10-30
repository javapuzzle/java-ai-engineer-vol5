package com.acme.retail.architecture.ch12;

/**
 * One real request to the global RAG platform: which tenant, which
 * region it originated in, which document category the query targets,
 * and how many resource units it will consume.
 */
public record GlobalQueryRequest(
        String tenantId, String region, String category, String queryTerm, long resourceUnits) {

    public GlobalQueryRequest {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId must not be blank");
        }
        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException("region must not be blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category must not be blank");
        }
        if (queryTerm == null || queryTerm.isBlank()) {
            throw new IllegalArgumentException("queryTerm must not be blank");
        }
        if (resourceUnits <= 0) {
            throw new IllegalArgumentException("resourceUnits must be positive");
        }
    }
}
