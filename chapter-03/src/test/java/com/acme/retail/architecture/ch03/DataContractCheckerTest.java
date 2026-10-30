package com.acme.retail.architecture.ch03;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DataContractCheckerTest {

    private final DataContractChecker checker = new DataContractChecker();

    private DataContract opsAgentOrderContract() {
        return new DataContract("OrderService", "OpsAgent", List.of(
                new ContractField("orderId", FieldType.STRING, true),
                new ContractField("customerId", FieldType.STRING, true),
                new ContractField("totalAmount", FieldType.DECIMAL, true),
                new ContractField("promotionCode", FieldType.STRING, false)));
    }

    @Test
    void reportsNoViolationsWhenTheSchemaSatisfiesTheContract() {
        ProducerSchema schema = new ProducerSchema("OrderService", Map.of(
                "orderId", FieldType.STRING,
                "customerId", FieldType.STRING,
                "totalAmount", FieldType.DECIMAL,
                "status", FieldType.STRING));

        List<ContractViolation> violations = checker.check(opsAgentOrderContract(), schema);

        assertThat(violations).isEmpty();
        assertThat(checker.isCompatible(opsAgentOrderContract(), schema)).isTrue();
    }

    @Test
    void catchesARequiredFieldSilentlyRemovedFromTheSchema() {
        ProducerSchema schema = new ProducerSchema("OrderService", Map.of(
                "orderId", FieldType.STRING,
                "customerId", FieldType.STRING,
                "status", FieldType.STRING));

        List<ContractViolation> violations = checker.check(opsAgentOrderContract(), schema);

        assertThat(violations).hasSize(1);
        ContractViolation violation = violations.get(0);
        assertThat(violation.fieldName()).isEqualTo("totalAmount");
        assertThat(violation.violationType()).isEqualTo(ContractViolation.ViolationType.MISSING_REQUIRED_FIELD);
    }

    @Test
    void catchesARequiredFieldRetypedInTheSchema() {
        ProducerSchema schema = new ProducerSchema("OrderService", Map.of(
                "orderId", FieldType.STRING,
                "customerId", FieldType.STRING,
                "totalAmount", FieldType.STRING));

        List<ContractViolation> violations = checker.check(opsAgentOrderContract(), schema);

        assertThat(violations).hasSize(1);
        ContractViolation violation = violations.get(0);
        assertThat(violation.fieldName()).isEqualTo("totalAmount");
        assertThat(violation.violationType()).isEqualTo(ContractViolation.ViolationType.TYPE_MISMATCH);
        assertThat(violation.reason()).contains("STRING").contains("DECIMAL");
    }

    @Test
    void doesNotFlagAMissingOptionalField() {
        ProducerSchema schema = new ProducerSchema("OrderService", Map.of(
                "orderId", FieldType.STRING,
                "customerId", FieldType.STRING,
                "totalAmount", FieldType.DECIMAL));

        List<ContractViolation> violations = checker.check(opsAgentOrderContract(), schema);

        assertThat(violations).isEmpty();
    }

    @Test
    void reportsEveryViolationWhenMultipleFieldsBreakTheContract() {
        ProducerSchema schema = new ProducerSchema("OrderService", Map.of(
                "orderId", FieldType.INTEGER,
                "status", FieldType.STRING));

        List<ContractViolation> violations = checker.check(opsAgentOrderContract(), schema);

        assertThat(violations).hasSize(3);
        assertThat(violations).extracting(ContractViolation::fieldName)
                .containsExactlyInAnyOrder("orderId", "customerId", "totalAmount");
    }
}
