package com.acme.retail.architecture.ch05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A sharded document index: documents are partitioned across a fixed
 * number of shards by category, and a query for a specific category is
 * routed to only the one shard that could possibly contain a relevant
 * answer -- the mechanism that makes "search millions of documents"
 * mean something other than "scan every document for every query."
 */
public class ShardedDocumentIndex {

    private final ShardRouter router;
    private final Map<Integer, DocumentShard> shardsById = new HashMap<>();

    public ShardedDocumentIndex(ShardRouter router) {
        this.router = router;
    }

    public void index(IndexedDocument document) {
        int shardId = router.shardFor(document.category());
        shardsById.computeIfAbsent(shardId, DocumentShard::new).add(document);
    }

    /**
     * Routes directly to the one shard the given category maps to.
     */
    public ShardSearchResult searchByCategory(String category, String term) {
        int shardId = router.shardFor(category);
        DocumentShard shard = shardsById.get(shardId);
        if (shard == null) {
            return new ShardSearchResult(List.of(), 0);
        }
        return new ShardSearchResult(shard.search(term), 1);
    }

    /**
     * Scans every populated shard, ignoring category routing entirely --
     * kept only as the baseline this chapter's routing is compared
     * against.
     */
    public ShardSearchResult searchAllShards(String term) {
        List<IndexedDocument> matches = new ArrayList<>();
        for (DocumentShard shard : shardsById.values()) {
            matches.addAll(shard.search(term));
        }
        return new ShardSearchResult(matches, shardsById.size());
    }

    public int populatedShardCount() {
        return shardsById.size();
    }
}
