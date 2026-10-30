package com.acme.retail.architecture.ch12;

import java.util.List;

/**
 * The result of one global platform request: whether it was allowed
 * through tenant isolation, which region's shard actually served it,
 * how many shards that search touched, and where the model invocation
 * was actually served from.
 */
public record GlobalQueryResult(
        boolean allowed,
        String isolationReason,
        List<IndexedDocument> matches,
        int shardsSearched,
        String servedByLocation) {
}
