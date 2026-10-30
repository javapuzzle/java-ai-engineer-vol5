package com.acme.retail.architecture.ch03;

/**
 * The field types this chapter's data contracts understand. Deliberately
 * small and explicit -- a real schema registry would have more, but the
 * mechanism this chapter teaches doesn't need them to prove the point.
 */
public enum FieldType {
    STRING,
    INTEGER,
    DECIMAL,
    BOOLEAN,
    TIMESTAMP
}
