package com.acme.retail.architecture.ch07;

/**
 * The result of a model invocation, including exactly where it was
 * actually served from -- the one piece of information the calling code
 * never had to ask for in advance.
 */
public record ModelInvocationResult(String responseText, String servedByLocation) {
}
