package com.acme.retail.architecture.ch03;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks a producer's real, current schema against a consumer's declared
 * data contract, and reports every field the schema fails to satisfy --
 * this is the mechanical check that catches a breaking schema change
 * before it ships, instead of relying on the consumer team noticing in
 * production.
 */
public class DataContractChecker {

    public List<ContractViolation> check(DataContract contract, ProducerSchema schema) {
        List<ContractViolation> violations = new ArrayList<>();

        for (ContractField field : contract.fields()) {
            FieldType actualType = schema.actualFields().get(field.name());

            if (actualType == null) {
                if (field.required()) {
                    violations.add(new ContractViolation(
                            field.name(),
                            ContractViolation.ViolationType.MISSING_REQUIRED_FIELD,
                            "Required field '" + field.name() + "' is missing from "
                                    + schema.producerName() + "'s schema"));
                }
                continue;
            }

            if (actualType != field.type()) {
                violations.add(new ContractViolation(
                        field.name(),
                        ContractViolation.ViolationType.TYPE_MISMATCH,
                        "Field '" + field.name() + "' is " + actualType
                                + " in " + schema.producerName() + "'s schema, but the contract expects "
                                + field.type()));
            }
        }

        return violations;
    }

    public boolean isCompatible(DataContract contract, ProducerSchema schema) {
        return check(contract, schema).isEmpty();
    }
}
