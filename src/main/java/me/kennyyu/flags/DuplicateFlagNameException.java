package me.kennyyu.flags;

public class DuplicateFlagNameException extends FlagException {

  private static final long serialVersionUID = -4479837518817046513L;

  public DuplicateFlagNameException(String flagName) {
    super("flag name deplicated: " + flagName);
  }

}
