package com.acme.retail.architecture.ch16;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class BuildVsBuyScorerTest {

    private final List<ScoringCriterion> criteria = List.of(
            new ScoringCriterion("COST", 0.40),
            new ScoringCriterion("CONTROL", 0.20),
            new ScoringCriterion("TIME_TO_MARKET", 0.25),
            new ScoringCriterion("MAINTENANCE_BURDEN", 0.15));

    private final BuildVsBuyScorer scorer = new BuildVsBuyScorer(criteria);

    @Test
    void computesTheExactWeightedScoreForAnOption() {
        OptionScore buildOption = new OptionScore("Build a custom vector store", Map.of(
                "COST", 4, "CONTROL", 9, "TIME_TO_MARKET", 3, "MAINTENANCE_BURDEN", 3));

        // 4*0.40 + 9*0.20 + 3*0.25 + 3*0.15 = 1.6 + 1.8 + 0.75 + 0.45 = 4.6
        assertThat(scorer.weightedScore(buildOption)).isCloseTo(4.6, within(0.0001));
    }

    @Test
    void recommendsBuyWhenItsWeightedScoreIsHigher() {
        OptionScore buildOption = new OptionScore("Build", Map.of(
                "COST", 4, "CONTROL", 9, "TIME_TO_MARKET", 3, "MAINTENANCE_BURDEN", 3));
        OptionScore buyOption = new OptionScore("Buy", Map.of(
                "COST", 7, "CONTROL", 4, "TIME_TO_MARKET", 9, "MAINTENANCE_BURDEN", 8));

        BuildVsBuyRecommendation recommendation = scorer.recommend(buildOption, buyOption);

        assertThat(recommendation.recommendedOption()).isEqualTo("BUY");
        assertThat(recommendation.buildScore()).isCloseTo(4.6, within(0.0001));
        assertThat(recommendation.buyScore()).isCloseTo(7.05, within(0.0001));
    }

    @Test
    void recommendsBuildWhenItsWeightedScoreIsStrictlyHigher() {
        OptionScore buildOption = new OptionScore("Build", Map.of(
                "COST", 8, "CONTROL", 10, "TIME_TO_MARKET", 8, "MAINTENANCE_BURDEN", 8));
        OptionScore buyOption = new OptionScore("Buy", Map.of(
                "COST", 3, "CONTROL", 2, "TIME_TO_MARKET", 3, "MAINTENANCE_BURDEN", 3));

        BuildVsBuyRecommendation recommendation = scorer.recommend(buildOption, buyOption);

        assertThat(recommendation.recommendedOption()).isEqualTo("BUILD");
    }

    @Test
    void aTieIsResolvedInFavorOfBuying() {
        OptionScore buildOption = new OptionScore("Build", Map.of(
                "COST", 5, "CONTROL", 5, "TIME_TO_MARKET", 5, "MAINTENANCE_BURDEN", 5));
        OptionScore buyOption = new OptionScore("Buy", Map.of(
                "COST", 5, "CONTROL", 5, "TIME_TO_MARKET", 5, "MAINTENANCE_BURDEN", 5));

        BuildVsBuyRecommendation recommendation = scorer.recommend(buildOption, buyOption);

        assertThat(recommendation.buildScore()).isCloseTo(recommendation.buyScore(), within(0.0001));
        assertThat(recommendation.recommendedOption()).isEqualTo("BUY");
    }

    @Test
    void rejectsCriteriaWhoseWeightsDoNotSumToOne() {
        assertThatThrownBy(() -> new BuildVsBuyScorer(List.of(
                new ScoringCriterion("COST", 0.5),
                new ScoringCriterion("CONTROL", 0.3))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("sum to 1.0");
    }

    @Test
    void throwsWhenAnOptionIsMissingAScoreForARegisteredCriterion() {
        OptionScore incomplete = new OptionScore("Incomplete", Map.of("COST", 5, "CONTROL", 5));

        assertThatThrownBy(() -> scorer.weightedScore(incomplete))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TIME_TO_MARKET");
    }

    @Test
    void rejectsAnOutOfRangeRawScore() {
        assertThatThrownBy(() -> new OptionScore("Bad", Map.of("COST", 11)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new OptionScore("Bad", Map.of("COST", 0)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
