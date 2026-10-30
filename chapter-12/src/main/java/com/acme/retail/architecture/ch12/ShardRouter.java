package com.acme.retail.architecture.ch12;

/**
 * Chapter 5's shard router, reused here to partition each region's own
 * document set by category.
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
