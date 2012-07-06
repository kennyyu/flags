package me.kennyyu.flags;

/**
 * Exception thrown when multiple flags, possibly from different files, have
 * conflicting names or alternate names.
 *
 * @author kennyyu (Kenny Yu)
 */
public class DuplicateFlagNameException extends FlagException {

  private static final long serialVersionUID = -4479837518817046513L;

  public DuplicateFlagNameException(String flagName) {
    super("flag name deplicated: " + flagName);
  }

}
