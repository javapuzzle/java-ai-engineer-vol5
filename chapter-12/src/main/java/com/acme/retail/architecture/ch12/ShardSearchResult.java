package com.acme.retail.architecture.ch12;

import java.util.List;

public record ShardSearchResult(List<IndexedDocument> matches, int shardsSearched) {
}
