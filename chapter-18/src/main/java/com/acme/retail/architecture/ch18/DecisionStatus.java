package com.acme.retail.architecture.ch18;

/**
 * The real, current status of one architecture decision record --
 * an ADR is never silently "just true forever"; it moves through a
 * real, explicit lifecycle.
 */
public enum DecisionStatus {
    PROPOSED,
    ACCEPTED,
    SUPERSEDED,
    DEPRECATED
}
