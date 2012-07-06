package me.kennyyu.flags;

/**
 * Super type of all exceptions thrown by this flag library.
 *
 * @author kennyyu (Kenny Yu)
 */
public class FlagException extends Exception {

  private static final long serialVersionUID = 3109773292095091239L;

  public FlagException(String message) {
    super(message);
  }

  public FlagException(String message, Throwable cause) {
    super(message, cause);
  }

  public FlagException(Throwable cause) {
    super(cause);
  }

}
