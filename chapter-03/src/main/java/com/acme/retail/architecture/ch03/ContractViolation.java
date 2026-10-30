package com.acme.retail.architecture.ch03;

/**
 * One way a producer's actual schema fails to satisfy a consumer's
 * declared contract.
 */
public record ContractViolation(String fieldName, ViolationType violationType, String reason) {

    public enum ViolationType {
        MISSING_REQUIRED_FIELD,
        TYPE_MISMATCH
    }
}
