package com.acme.retail.architecture.ch20;

import java.util.List;

public record TargetArchitectureDecision(
        boolean allowed, String reason, List<ResidencyConflict> residencyConflicts) {
}
