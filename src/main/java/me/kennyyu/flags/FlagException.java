package me.kennyyu.flags;

public class FlagException extends RuntimeException {

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
