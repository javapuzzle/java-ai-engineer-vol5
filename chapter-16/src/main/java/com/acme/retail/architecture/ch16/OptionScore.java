package com.acme.retail.architecture.ch16;

import java.util.Map;

/**
 * One real option's raw score against every criterion, on a 1-10 scale
 * where higher is always better for that criterion (a high maintenance
 * -burden score means low real burden, not high burden).
 */
public record OptionScore(String optionName, Map<String, Integer> rawScoresByCriterion) {

    public OptionScore {
        if (optionName == null || optionName.isBlank()) {
            throw new IllegalArgumentException("optionName must not be blank");
        }
        for (int score : rawScoresByCriterion.values()) {
            if (score < 1 || score > 10) {
                throw new IllegalArgumentException("Every raw score must be between 1 and 10");
            }
        }
        rawScoresByCriterion = Map.copyOf(rawScoresByCriterion);
    }
}
