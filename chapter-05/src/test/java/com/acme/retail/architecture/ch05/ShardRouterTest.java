package com.acme.retail.architecture.ch05;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShardRouterTest {

    @Test
    void routesTheSameKeyToTheSameShardEveryTime() {
        ShardRouter router = new ShardRouter(4);

        int firstCall = router.shardFor("returns");
        int secondCall = router.shardFor("returns");

        assertThat(firstCall).isEqualTo(secondCall);
    }

    @Test
    void neverReturnsAShardIndexOutsideTheConfiguredRange() {
        ShardRouter router = new ShardRouter(4);

        for (int i = 0; i < 50; i++) {
            int shard = router.shardFor("category-" + i);
            assertThat(shard).isBetween(0, 3);
        }
    }

    @Test
    void rejectsANonPositiveShardCount() {
        assertThatThrownBy(() -> new ShardRouter(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ShardRouter(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsABlankPartitionKey() {
        ShardRouter router = new ShardRouter(4);
        assertThatThrownBy(() -> router.shardFor(" ")).isInstanceOf(IllegalArgumentException.class);
    }
}
