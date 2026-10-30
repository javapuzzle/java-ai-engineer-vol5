package com.acme.retail.architecture.ch14;

/**
 * The real action taken for a given risk score. Three tiers, not two --
 * REVIEW exists specifically so a merely elevated risk doesn't force an
 * immediate binary approve-or-block choice.
 */
public enum FraudDecision {
    APPROVE,
    REVIEW,
    BLOCK
}
