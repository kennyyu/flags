package me.kennyyu.flags;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link UnknownFlagNameException}
 *
 * @author kennyyu (Kenny Yu)
 */
public class UnknownFlagNameExceptionTest {

  @Test
  public void testUnknownFlagTypeExceptionTest() {
    String[] args = {"--unknownflag"};
    try {
      Flags.parseWithExceptions(args, "testing");
      fail("did not throw UnknownFlagNameException");
    } catch (UnknownFlagNameException e) {
      assertTrue(true);
    } catch (FlagException e) {
      fail("threw unexpected exception: " + e);
    }
  }

}
