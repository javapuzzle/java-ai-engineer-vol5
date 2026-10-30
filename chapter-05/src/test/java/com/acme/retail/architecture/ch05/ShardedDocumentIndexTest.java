package com.acme.retail.architecture.ch05;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ShardedDocumentIndexTest {

    @Test
    void routingToASpecificCategorySearchesExactlyOneShard() {
        ShardRouter router = new ShardRouter(4);
        ShardedDocumentIndex index = new ShardedDocumentIndex(router);

        index.index(new IndexedDocument("doc-1", "returns", "How to process a refund for a returned item."));
        index.index(new IndexedDocument("doc-2", "returns", "Refund timelines for damaged goods."));
        index.index(new IndexedDocument("doc-3", "warranty", "Warranty claims do not involve a refund."));

        ShardSearchResult result = index.searchByCategory("returns", "refund");

        assertThat(result.shardsSearched()).isEqualTo(1);
        assertThat(result.matches()).extracting(IndexedDocument::id)
                .contains("doc-1", "doc-2");
    }

    @Test
    void searchingAnUnindexedCategoryTouchesZeroShardsAndReturnsNoMatches() {
        ShardRouter router = new ShardRouter(4);
        ShardedDocumentIndex index = new ShardedDocumentIndex(router);

        index.index(new IndexedDocument("doc-1", "returns", "Refund process."));

        int returnsShard = router.shardFor("returns");
        String emptyCategory = findCategoryMappingToADifferentShard(router, returnsShard);

        ShardSearchResult result = index.searchByCategory(emptyCategory, "refund");

        assertThat(result.shardsSearched()).isEqualTo(0);
        assertThat(result.matches()).isEmpty();
    }

    private static String findCategoryMappingToADifferentShard(ShardRouter router, int shardToAvoid) {
        for (int i = 0; i < 1000; i++) {
            String candidate = "empty-category-" + i;
            if (router.shardFor(candidate) != shardToAvoid) {
                return candidate;
            }
        }
        throw new IllegalStateException("Could not find a non-colliding category for this test");
    }

    @Test
    void routedSearchTouchesFewerShardsThanScanningEveryShard() {
        ShardRouter router = new ShardRouter(4);
        ShardedDocumentIndex index = new ShardedDocumentIndex(router);

        Set<Integer> distinctShardsUsed = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String category = "category-" + i;
            distinctShardsUsed.add(router.shardFor(category));
            index.index(new IndexedDocument("doc-" + i, category, "widget assembly instructions"));
        }

        // 100 categories hashed across 4 shards populate every shard.
        assertThat(distinctShardsUsed).hasSize(4);
        assertThat(index.populatedShardCount()).isEqualTo(4);

        ShardSearchResult routed = index.searchByCategory("category-0", "widget");
        ShardSearchResult fullScan = index.searchAllShards("widget");

        assertThat(routed.shardsSearched()).isEqualTo(1);
        assertThat(fullScan.shardsSearched()).isEqualTo(4);
        assertThat(routed.shardsSearched()).isLessThan(fullScan.shardsSearched());
        assertThat(fullScan.matches()).hasSize(100);
    }

    @Test
    void searchIsCaseInsensitiveAndMatchesOnSubstring() {
        ShardRouter router = new ShardRouter(2);
        ShardedDocumentIndex index = new ShardedDocumentIndex(router);
        index.index(new IndexedDocument("doc-1", "billing", "Your INVOICE is attached."));

        ShardSearchResult result = index.searchByCategory("billing", "invoice");

        assertThat(result.matches()).extracting(IndexedDocument::id).containsExactly("doc-1");
    }

    @Test
    void rejectsInvalidIndexedDocuments() {
        assertThatBlankFieldsAreRejected();
    }

    private void assertThatBlankFieldsAreRejected() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                new IndexedDocument(" ", "returns", "content"))
                .isInstanceOf(IllegalArgumentException.class);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                new IndexedDocument("doc-1", " ", "content"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
