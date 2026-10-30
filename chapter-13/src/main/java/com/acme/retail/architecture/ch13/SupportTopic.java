package com.acme.retail.architecture.ch13;

/**
 * The support topics this system's specialists cover. A topic with no
 * matching specialist falls back to the general specialist -- never to
 * an unhandled request.
 */
public enum SupportTopic {
    BILLING,
    SHIPPING,
    RETURNS,
    GENERAL
}
