package me.kennyyu.flags;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for {@link IllegalFlagAnnotationException}
 *
 * @author kennyyu (Kenny Yu)
 */
public class IllegalFlagAnnotationExceptionTest {

  @SuppressWarnings("unused")
  @FlagInfo(help = "illegal flag annotation", environment = "annotation")
  private static Object object = null;

  @Test
  public void testIllegalFlagAnnotationException() {
    String[] args = {"--object=null"};
    try {
      Flags.parseWithExceptions(args, "annotation");
      fail("did not throw IllegalFlagAnnotationException");
    } catch (IllegalFlagAnnotationException e) {
      assertTrue(true);
    } catch (FlagException e) {
      fail("threw unexpected exception: " + e);
    }
  }

}
