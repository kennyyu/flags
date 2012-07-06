package me.kennyyu.flags;

/**
 * Exception thrown when a command line string is formatted illegally.
 *
 * @author kennyyu (Kenny Yu)
 */
public class IllegalFlagFormatException extends FlagException {

  private static final long serialVersionUID = -953082366202377632L;

  public IllegalFlagFormatException(String illegalString) {
    super("illegally formatted string in Map: " + illegalString);
  }

}
