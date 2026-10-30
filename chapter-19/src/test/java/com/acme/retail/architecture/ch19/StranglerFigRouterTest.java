package com.acme.retail.architecture.ch19;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StranglerFigRouterTest {

    @Test
    void aZeroPercentRolloutAlwaysRoutesToTheLegacyPath() {
        StranglerFigRouter router = new StranglerFigRouter(
                Map.of("InventoryLookup", new MigrationRolloutPolicy("InventoryLookup", 0)));

        for (int i = 0; i < 50; i++) {
            RoutingTarget target = router.routeRequest("InventoryLookup", "req-" + i);
            assertThat(target).isEqualTo(RoutingTarget.LEGACY_PATH);
        }
    }

    @Test
    void aHundredPercentRolloutAlwaysRoutesToTheNewPath() {
        StranglerFigRouter router = new StranglerFigRouter(
                Map.of("InventoryLookup", new MigrationRolloutPolicy("InventoryLookup", 100)));

        for (int i = 0; i < 50; i++) {
            RoutingTarget target = router.routeRequest("InventoryLookup", "req-" + i);
            assertThat(target).isEqualTo(RoutingTarget.NEW_PATH);
        }
    }

    @Test
    void aFiftyPercentRolloutSplitsRealTrafficRoughlyInHalf() {
        StranglerFigRouter router = new StranglerFigRouter(
                Map.of("InventoryLookup", new MigrationRolloutPolicy("InventoryLookup", 50)));

        int newPathCount = 0;
        int legacyPathCount = 0;
        for (int i = 0; i < 1000; i++) {
            RoutingTarget target = router.routeRequest("InventoryLookup", "req-" + i);
            if (target == RoutingTarget.NEW_PATH) {
                newPathCount++;
            } else {
                legacyPathCount++;
            }
        }

        assertThat(newPathCount + legacyPathCount).isEqualTo(1000);
        assertThat(newPathCount).isBetween(400, 600);
        assertThat(legacyPathCount).isBetween(400, 600);
    }

    @Test
    void theSameRequestIdAlwaysRoutesToTheSameTargetForAGivenPolicy() {
        StranglerFigRouter router = new StranglerFigRouter(
                Map.of("InventoryLookup", new MigrationRolloutPolicy("InventoryLookup", 50)));

        RoutingTarget first = router.routeRequest("InventoryLookup", "req-42");
        RoutingTarget second = router.routeRequest("InventoryLookup", "req-42");

        assertThat(first).isEqualTo(second);
    }

    @Test
    void throwsForAComponentWithNoRegisteredRolloutPolicy() {
        StranglerFigRouter router = new StranglerFigRouter(
                Map.of("InventoryLookup", new MigrationRolloutPolicy("InventoryLookup", 50)));

        assertThatThrownBy(() -> router.routeRequest("OrderCore", "req-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("OrderCore");
    }
}
