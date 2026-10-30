package com.acme.retail.architecture.ch05;

/**
 * Deterministically maps a partition key (here, a document category) to
 * one of a fixed number of shards. The same key always routes to the
 * same shard, which is what lets a query skip every shard that couldn't
 * possibly contain a relevant answer.
 */
public class ShardRouter {

    private final int shardCount;

    public ShardRouter(int shardCount) {
        if (shardCount <= 0) {
            throw new IllegalArgumentException("shardCount must be positive");
        }
        this.shardCount = shardCount;
    }

    public int shardCount() {
        return shardCount;
    }

    public int shardFor(String partitionKey) {
        if (partitionKey == null || partitionKey.isBlank()) {
            throw new IllegalArgumentException("partitionKey must not be blank");
        }
        return Math.floorMod(partitionKey.hashCode(), shardCount);
    }
}
