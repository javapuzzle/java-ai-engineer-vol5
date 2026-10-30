package com.acme.retail.architecture.ch11;

/**
 * The category of system property a derived constraint actually
 * constrains -- deliberately the same vocabulary this volume's earlier
 * chapters already gave real meaning to (latency, availability, and so
 * on), not a fresh, disconnected taxonomy.
 */
public enum ConstraintCategory {
    LATENCY,
    AVAILABILITY,
    DATA_RESIDENCY,
    COST,
    SECURITY
}
