package com.acme.retail.architecture.ch19;

/**
 * Which real path a given request was actually routed to during a
 * strangler-fig migration.
 */
public enum RoutingTarget {
    LEGACY_PATH,
    NEW_PATH
}
