package com.acme.retail.architecture.ch12;

import java.util.ArrayList;
import java.util.List;

public class DocumentShard {

    private final int shardId;
    private final List<IndexedDocument> documents = new ArrayList<>();

    public DocumentShard(int shardId) {
        this.shardId = shardId;
    }

    public int shardId() {
        return shardId;
    }

    public void add(IndexedDocument document) {
        documents.add(document);
    }

    public List<IndexedDocument> search(String term) {
        List<IndexedDocument> matches = new ArrayList<>();
        for (IndexedDocument document : documents) {
            if (document.content().toLowerCase().contains(term.toLowerCase())) {
                matches.add(document);
            }
        }
        return matches;
    }

    public int documentCount() {
        return documents.size();
    }
}
