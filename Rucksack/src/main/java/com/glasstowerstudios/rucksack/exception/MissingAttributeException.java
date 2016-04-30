package com.glasstowerstudios.rucksack.exception;

/**
 * A {@link RuntimeException} indicating that a style attribute was required, but is missing.
 */
public class MissingAttributeException extends RuntimeException {
  public MissingAttributeException(String attributeName) {
    super(attributeName + " is a required attribute");
  }
}
