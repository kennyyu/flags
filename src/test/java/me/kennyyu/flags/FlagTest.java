package me.kennyyu.flags;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FlagTest {

  @FlagDesc(help = "flagInteger", altName = "int")
  private static Flag<Integer> flagInteger = Flags.valueOf(0);

  @FlagDesc(help = "flagLong", altName = "long")
  private static Flag<Long> flagLong = Flags.valueOf(0L);

  @FlagDesc(help = "flagDouble", altName = "double")
  private static Flag<Double> flagDouble = Flags.valueOf(0.0);

  @FlagDesc(help = "flagFloat", altName = "float")
  private static Flag<Float> flagFloat = Flags.valueOf((float) 0.0);

  @FlagDesc(help = "flagBoolean", altName = "boolean")
  private static Flag<Boolean> flagBoolean = Flags.valueOf(false);

  @FlagDesc(help = "flagCharacter", altName = "char")
  private static Flag<Character> flagCharacter = Flags.valueOf('\0');

  @FlagDesc(help = "flagString", altName = "string")
  private static Flag<String> flagString = Flags.valueOf("");

  @FlagDesc(help = "flagByte", altName = "byte")
  private static Flag<Byte> flagByte = Flags.valueOf((byte) 0x00);

  @FlagDesc(help = "flagAlt", altName = "short")
  private static Flag<Short> flagShort = Flags.valueOf((short) 0);

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
    flagInteger = Flags.valueOf(0);
    flagLong = Flags.valueOf(0L);
    flagDouble = Flags.valueOf(0.0);
    flagFloat = Flags.valueOf((float) 0.0);
    flagBoolean = Flags.valueOf(false);
    flagCharacter = Flags.valueOf('\0');
    flagString = Flags.valueOf("");
    flagByte = Flags.valueOf((byte) 0x00);
    flagShort = Flags.valueOf((short) 0);
  }

  @Test
  public void testIntegerFull() {
    String args[] = {"--flagInteger=7"};
    Flags.parse(args);
    assertEquals(7, flagInteger.get());
  }

  @Test
  public void testIntegerAlt() {
    String args[] = {"-int=-5"};
    Flags.parse(args);
    assertEquals(-5, flagInteger.get());
  }

  @Test
  public void testLongFull() {
    String args[] = {"--flagLong=7"};
    Flags.parse(args);
    assertEquals(7L, flagLong.get());
  }

  @Test
  public void testLongAlt() {
    String args[] = {"-long=-5"};
    Flags.parse(args);
    assertEquals(-5L, flagLong.get());
  }

  @Test
  public void testDoubleFull() {
    String args[] = {"--flagDouble=5.6"};
    Flags.parse(args);
    assertEquals(5.6, flagDouble.get());
  }

  @Test
  public void testDoubleAlt() {
    String args[] = {"-double=-5.4"};
    Flags.parse(args);
    assertEquals(-5.4, flagDouble.get());
  }

  @Test
  public void testFloatFull() {
    String args[] = {"--flagFloat=5.6"};
    Flags.parse(args);
    assertEquals((float) 5.6, flagFloat.get());
  }

  @Test
  public void testFloatAlt() {
    String args[] = {"-float=-5.4"};
    Flags.parse(args);
    assertEquals((float) -5.4, flagFloat.get());
  }

  @Test
  public void testBooleanFull() {
    String args[] = {"--flagBoolean=true"};
    Flags.parse(args);
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanAlt() {
    String args[] = {"-boolean=true"};
    Flags.parse(args);
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanFullDefault() {
    String args[] = {"--flagBoolean"};
    Flags.parse(args);
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanAltDefault() {
    String args[] = {"-boolean"};
    Flags.parse(args);
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testCharacterFull() {
    String args[] = {"--flagCharacter=c"};
    Flags.parse(args);
    assertEquals('c', flagCharacter.get());
  }

  @Test
  public void testCharacterAlt() {
    String args[] = {"-char=d"};
    Flags.parse(args);
    assertEquals('d', flagCharacter.get());
  }

  @Test
  public void testStringFull() {
    String args[] = {"--flagString=bar"};
    Flags.parse(args);
    assertEquals("bar", flagString.get());
  }

  @Test
  public void testStringAlt() {
    String args[] = {"-string=foo"};
    Flags.parse(args);
    assertEquals("foo", flagString.get());
  }

  @Test
  public void testByteFull() {
    String args[] = {"--flagByte=AB"};
    Flags.parse(args);
    assertEquals((byte) 0xAB, flagByte.get());
  }

  @Test
  public void testByteAlt() {
    String args[] = {"-byte=-AB"};
    Flags.parse(args);
    assertEquals((byte) -0xAB, flagByte.get());
  }

  @Test
  public void testShortFull() {
    String args[] = {"--flagShort=2"};
    Flags.parse(args);
    assertEquals((short) 2, flagShort.get());
  }

  @Test
  public void testShortAlt() {
    String args[] = {"-short=-2"};
    Flags.parse(args);
    assertEquals((short) -2, flagShort.get());
  }
}
