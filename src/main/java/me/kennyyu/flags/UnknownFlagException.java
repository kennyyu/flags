package me.kennyyu.flags;

public class UnknownFlagException extends FlagException {

  private static final long serialVersionUID = 1401720153142656300L;

  public UnknownFlagException(String flagName) {
    super("unknown flag: " + flagName);
  }

}
