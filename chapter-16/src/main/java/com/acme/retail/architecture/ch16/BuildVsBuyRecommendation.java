package com.acme.retail.architecture.ch16;

/**
 * The real, computed outcome of comparing build and buy: both weighted
 * scores, and which option actually wins -- never a gut call presented
 * as if it were a calculation.
 */
public record BuildVsBuyRecommendation(String recommendedOption, double buildScore, double buyScore) {
}
