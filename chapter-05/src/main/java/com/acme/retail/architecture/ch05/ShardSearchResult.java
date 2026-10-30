package com.acme.retail.architecture.ch05;

import java.util.List;

/**
 * The result of a search: the matching documents, and how many shards
 * actually had to be searched to find them -- the number this chapter's
 * routing is designed to keep as small as possible.
 */
public record ShardSearchResult(List<IndexedDocument> matches, int shardsSearched) {
}
