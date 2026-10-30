package com.acme.retail.architecture.ch14;

/**
 * An honest, explicit, rule-based risk score -- a real, working stand-in
 * for a live ML fraud model, exactly labeled as what it is. Each real
 * signal contributes a fixed, documented weight; nothing here pretends
 * to be a trained model.
 */
public class FraudRiskScorer {

    static final double AMOUNT_WEIGHT = 0.30;
    static final double NEW_DEVICE_WEIGHT = 0.25;
    static final double NEW_LOCATION_WEIGHT = 0.25;
    static final double VELOCITY_WEIGHT = 0.30;

    static final double HIGH_AMOUNT_THRESHOLD = 1000.0;
    static final int HIGH_VELOCITY_THRESHOLD = 5;

    public double score(TransactionSignal signal) {
        double score = 0.0;

        if (signal.amount() > HIGH_AMOUNT_THRESHOLD) {
            score += AMOUNT_WEIGHT;
        }
        if (signal.newDevice()) {
            score += NEW_DEVICE_WEIGHT;
        }
        if (signal.newLocation()) {
            score += NEW_LOCATION_WEIGHT;
        }
        if (signal.velocityLastHour() > HIGH_VELOCITY_THRESHOLD) {
            score += VELOCITY_WEIGHT;
        }

        return Math.min(score, 1.0);
    }
}
