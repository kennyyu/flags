package me.kennyyu.flags;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for {@link UnsupportedFlagTypeException}
 *
 * @author kennyyu (Kenny Yu)
 */
public class UnsupportedFlagTypeExceptionTest {

  @SuppressWarnings("unused")
  @FlagInfo(help = "unsupported parameter type", environment = "parameter")
  private static Flag<Object> flagBad = Flags.valueOf(new Object());

  @Test
  public void testUnsupportedFlagTypeException() {
    String[] args = {"--flagBad=5"};
    try {
      Flags.parseWithExceptions(args, "parameter");
      fail("did not throw exception");
    } catch (UnsupportedFlagTypeException e) {
      assertTrue(true);
    } catch (FlagException e) {
      fail("threw unexpected exception: " + e);
    }
  }

}
