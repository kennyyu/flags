package me.kennyyu.flags;

public class IllegalStringFlagException extends FlagException {

  private static final long serialVersionUID = -953082366202377632L;

  public IllegalStringFlagException(String illegalString) {
    super("illegally formatted string in Map: " + illegalString);
  }

}
