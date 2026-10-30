package com.acme.retail.architecture.ch16;

import java.util.List;

/**
 * A real, weighted scoring matrix generalizing Chapter 8's build-vs-buy
 * discipline to any set of criteria -- cost, control, time-to-market,
 * maintenance burden, or anything else a team decides actually matters
 * for a given decision -- as long as the weights are explicit and sum
 * to a real whole.
 */
public class BuildVsBuyScorer {

    private final List<ScoringCriterion> criteria;

    public BuildVsBuyScorer(List<ScoringCriterion> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("At least one scoring criterion is required");
        }
        double totalWeight = criteria.stream().mapToDouble(ScoringCriterion::weight).sum();
        if (Math.abs(totalWeight - 1.0) > 0.0001) {
            throw new IllegalArgumentException(
                    "Criterion weights must sum to 1.0, but summed to " + totalWeight);
        }
        this.criteria = List.copyOf(criteria);
    }

    public double weightedScore(OptionScore option) {
        double total = 0.0;
        for (ScoringCriterion criterion : criteria) {
            Integer rawScore = option.rawScoresByCriterion().get(criterion.name());
            if (rawScore == null) {
                throw new IllegalArgumentException(
                        "Option '" + option.optionName() + "' is missing a score for criterion: "
                                + criterion.name());
            }
            total += rawScore * criterion.weight();
        }
        return total;
    }

    /**
     * A tie is resolved in favor of buying -- the same "simpler default
     * wins a tie" rule Chapter 8 applied to the managed API, since
     * buying is almost always the lower-commitment choice when the
     * weighted evidence doesn't clearly favor building instead.
     */
    public BuildVsBuyRecommendation recommend(OptionScore buildOption, OptionScore buyOption) {
        double buildScore = weightedScore(buildOption);
        double buyScore = weightedScore(buyOption);

        String recommended = buildScore > buyScore ? "BUILD" : "BUY";

        return new BuildVsBuyRecommendation(recommended, buildScore, buyScore);
    }
}
