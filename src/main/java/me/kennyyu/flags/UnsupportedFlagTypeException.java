package me.kennyyu.flags;

import java.lang.reflect.Type;

/**
 * Exception thrown when a {@link Flag} object contains an unsupported parameter
 * type.
 *
 * @author Kenny Yu
 */
public class UnsupportedFlagTypeException extends FlagException {

  private static final long serialVersionUID = 2391812264242282695L;

  public UnsupportedFlagTypeException(Type type) {
    super("unsupported flag parameter type: " + type.toString());
  }
}
