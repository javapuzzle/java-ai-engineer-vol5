package com.acme.retail.architecture.ch02;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Records a ledger entry for every tenant-affecting action, keyed by
 * action type. Knows nothing about any other subscriber.
 */
public class FinanceLedgerSubscriber {

    private static final Map<String, BigDecimal> LEDGER_AMOUNT_BY_ACTION_TYPE = Map.of(
            "REFUND", new BigDecimal("25.00"),
            "REPLACEMENT_SHIPMENT", new BigDecimal("40.00"));

    private final List<LedgerEntry> ledgerEntries = new ArrayList<>();

    public void onActionTaken(OpsAgentActionTakenEvent event) {
        BigDecimal amount = LEDGER_AMOUNT_BY_ACTION_TYPE.getOrDefault(
                event.actionType(), BigDecimal.ZERO);
        ledgerEntries.add(new LedgerEntry(event.tenantId(), event.orderId(), amount));
    }

    public List<LedgerEntry> ledgerEntries() {
        return List.copyOf(ledgerEntries);
    }

    public record LedgerEntry(String tenantId, String orderId, BigDecimal amount) {
    }
}
