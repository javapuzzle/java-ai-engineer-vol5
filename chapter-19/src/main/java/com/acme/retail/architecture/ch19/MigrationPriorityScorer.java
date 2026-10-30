package com.acme.retail.architecture.ch19;

import java.util.Comparator;
import java.util.List;

/**
 * Scores which legacy components are actually safe to migrate first --
 * rewarding real business value and penalizing real coupling, so a
 * migration plan starts with the components that are both worth doing
 * and least likely to break something else on the way.
 */
public class MigrationPriorityScorer {

    public int priorityScore(LegacyComponent component) {
        return component.businessValueScore() - component.couplingScore();
    }

    public List<LegacyComponent> prioritize(List<LegacyComponent> components) {
        return components.stream()
                .sorted(Comparator.comparingInt(this::priorityScore).reversed())
                .toList();
    }
}
