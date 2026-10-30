package com.acme.retail.architecture.ch19;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MigrationPriorityScorerTest {

    private final MigrationPriorityScorer scorer = new MigrationPriorityScorer();

    @Test
    void computesTheExactPriorityScoreAsValueMinusCoupling() {
        LegacyComponent component = new LegacyComponent("InventoryLookup", 2, 9);

        assertThat(scorer.priorityScore(component)).isEqualTo(7);
    }

    @Test
    void prioritizesHighValueLowCouplingComponentsFirst() {
        LegacyComponent easyAndValuable = new LegacyComponent("InventoryLookup", 2, 9);
        LegacyComponent hardAndValuable = new LegacyComponent("OrderCore", 9, 9);
        LegacyComponent easyAndLowValue = new LegacyComponent("ReportingStub", 2, 3);

        List<LegacyComponent> prioritized = scorer.prioritize(
                List.of(hardAndValuable, easyAndLowValue, easyAndValuable));

        assertThat(prioritized).extracting(LegacyComponent::name)
                .containsExactly("InventoryLookup", "ReportingStub", "OrderCore");
    }

    @Test
    void aHighlyCoupledComponentCanScoreBelowALessValuableButSaferOne() {
        LegacyComponent hardAndValuable = new LegacyComponent("OrderCore", 9, 9);
        LegacyComponent easyAndLowValue = new LegacyComponent("ReportingStub", 2, 3);

        assertThat(scorer.priorityScore(hardAndValuable)).isEqualTo(0);
        assertThat(scorer.priorityScore(easyAndLowValue)).isEqualTo(1);
    }
}
