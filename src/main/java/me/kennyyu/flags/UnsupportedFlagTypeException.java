package me.kennyyu.flags;

import java.lang.reflect.Type;

public class UnsupportedFlagTypeException extends FlagException {

  private static final long serialVersionUID = 2391812264242282695L;

  public UnsupportedFlagTypeException(Type type) {
    super("unsupported flag parameter type: " + type.toString());
  }
}
