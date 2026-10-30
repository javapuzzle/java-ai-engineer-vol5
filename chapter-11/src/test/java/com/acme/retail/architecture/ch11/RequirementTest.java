package com.acme.retail.architecture.ch11;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequirementTest {

    @Test
    void rejectsABlankId() {
        assertThatThrownBy(() -> new Requirement(" ", "Some description"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsABlankDescription() {
        assertThatThrownBy(() -> new Requirement("REQ-1", " "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
