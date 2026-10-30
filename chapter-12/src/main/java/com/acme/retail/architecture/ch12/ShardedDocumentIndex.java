package com.acme.retail.architecture.ch12;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ShardSearchResult searchByCategory(String category, String term) {
        int shardId = router.shardFor(category);
        DocumentShard shard = shardsById.get(shardId);
        if (shard == null) {
            return new ShardSearchResult(List.of(), 0);
        }
        return new ShardSearchResult(shard.search(term), 1);
    }

    public int populatedShardCount() {
        return shardsById.size();
    }
}
