package me.kennyyu.flags;

public class IllegalFlagFormatException extends FlagException {

  private static final long serialVersionUID = -953082366202377632L;

  public IllegalFlagFormatException(String illegalString) {
    super("illegally formatted string in Map: " + illegalString);
  }

}
