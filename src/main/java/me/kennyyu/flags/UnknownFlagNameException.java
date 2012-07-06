package me.kennyyu.flags;

public class UnknownFlagNameException extends FlagException {

  private static final long serialVersionUID = 1401720153142656300L;

  public UnknownFlagNameException(String flagName) {
    super("unknown flag: " + flagName);
  }

}
