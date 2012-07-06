package me.kennyyu.flags;

/**
 * Exception thrown when an unknown flag is passed at the command line.
 *
 * @author kennyyu (Kenny Yu)
 */
public class UnknownFlagNameException extends FlagException {

  private static final long serialVersionUID = 1401720153142656300L;

  public UnknownFlagNameException(String flagName) {
    super("unknown flag: " + flagName);
  }

}
