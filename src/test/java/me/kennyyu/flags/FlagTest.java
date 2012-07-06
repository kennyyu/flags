package me.kennyyu.flags;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Tests for {@link Flags}.
 *
 * @author kennyyu (Kenny Yu)
 */
public class FlagTest {

  @FlagInfo(help = "flagInteger", altName = "int")
  private static Flag<Integer> flagInteger = Flags.valueOf(0);

  @FlagInfo(help = "flagLong", altName = "long")
  private static Flag<Long> flagLong = Flags.valueOf(0L);

  @FlagInfo(help = "flagDouble", altName = "double")
  private static Flag<Double> flagDouble = Flags.valueOf(0.0);

  @FlagInfo(help = "flagFloat", altName = "float")
  private static Flag<Float> flagFloat = Flags.valueOf((float) 0.0);

  @FlagInfo(help = "flagBoolean", altName = "boolean")
  private static Flag<Boolean> flagBoolean = Flags.valueOf(false);

  @FlagInfo(help = "flagCharacter", altName = "char")
  private static Flag<Character> flagCharacter = Flags.valueOf('\0');

  @FlagInfo(help = "flagString", altName = "string")
  private static Flag<String> flagString = Flags.valueOf("");

  @FlagInfo(help = "flagByte", altName = "byte")
  private static Flag<Byte> flagByte = Flags.valueOf((byte) 0x00);

  @FlagInfo(help = "flagAlt", altName = "short")
  private static Flag<Short> flagShort = Flags.valueOf((short) 0);

  @FlagInfo(help = "flagList", altName = "list")
  private static Flag<List<Integer>> flagList =
      Flags.valueOf(Lists.<Integer>newArrayList());

  @FlagInfo(help = "flagSet", altName = "set")
  private static Flag<Set<String>> flagSet =
      Flags.valueOf(Sets.<String>newHashSet());

  @FlagInfo(help = "flagMap", altName = "map")
  private static Flag<Map<String, Integer>> flagMap =
      Flags.valueOf(Maps.<String, Integer>newHashMap());

  @FlagInfo(help = "flagEnum", altName = "enum")
  private static Flag<Day> flagEnum = Flags.valueOf(Day.SUNDAY);

  @FlagInfo(help = "flagSetEnum", altName = "setEnum")
  private static Flag<Set<Day>> flagSetEnum =
      Flags.valueOf(Sets.<Day>newHashSet());

  private static enum Day {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY
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
    flagList = Flags.valueOf(Lists.<Integer>newArrayList());
    flagSet = Flags.valueOf(Sets.<String>newHashSet());
    flagMap = Flags.valueOf(Maps.<String, Integer>newHashMap());
    flagEnum = Flags.valueOf(Day.SUNDAY);
    flagSetEnum = Flags.valueOf(Sets.<Day>newHashSet());
  }

  @Test
  public void testIntegerFull() {
    String[] args = {"--flagInteger=7"};
    Flags.parse(args);
    assertEquals(7, flagInteger.get());
  }

  @Test
  public void testIntegerAlt() {
    String[] args = {"-int=-5"};
    Flags.parse(args);
    assertEquals(-5, flagInteger.get());
  }

  @Test
  public void testLongFull() {
    String[] args = {"--flagLong=7"};
    Flags.parse(args);
    assertEquals(7L, flagLong.get());
  }

  @Test
  public void testLongAlt() {
    String[] args = {"-long=-5"};
    Flags.parse(args);
    assertEquals(-5L, flagLong.get());
  }

  @Test
  public void testDoubleFull() {
    String[] args = {"--flagDouble=5.6"};
    Flags.parse(args);
    assertEquals(5.6, flagDouble.get());
  }

  @Test
  public void testDoubleAlt() {
    String[] args = {"-double=-5.4"};
    Flags.parse(args);
    assertEquals(-5.4, flagDouble.get());
  }

  @Test
  public void testFloatFull() {
    String[] args = {"--flagFloat=5.6"};
    Flags.parse(args);
    assertEquals((float) 5.6, flagFloat.get());
  }

  @Test
  public void testFloatAlt() {
    String[] args = {"-float=-5.4"};
    Flags.parse(args);
    assertEquals((float) -5.4, flagFloat.get());
  }

  @Test
  public void testBooleanFull() {
    String[] args = {"--flagBoolean=true"};
    Flags.parse(args);
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanAlt() {
    String[] args = {"-boolean=true"};
    Flags.parse(args);
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanFullDefault() {
    String[] args = {"--flagBoolean"};
    Flags.parse(args);
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanAltDefault() {
    String[] args = {"-boolean"};
    Flags.parse(args);
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testCharacterFull() {
    String[] args = {"--flagCharacter=c"};
    Flags.parse(args);
    assertEquals('c', flagCharacter.get());
  }

  @Test
  public void testCharacterAlt() {
    String[] args = {"-char=d"};
    Flags.parse(args);
    assertEquals('d', flagCharacter.get());
  }

  @Test
  public void testStringFull() {
    String[] args = {"--flagString=bar"};
    Flags.parse(args);
    assertEquals("bar", flagString.get());
  }

  @Test
  public void testStringAlt() {
    String[] args = {"-string=foo"};
    Flags.parse(args);
    assertEquals("foo", flagString.get());
  }

  @Test
  public void testByteFull() {
    String[] args = {"--flagByte=AB"};
    Flags.parse(args);
    assertEquals((byte) 0xAB, flagByte.get());
  }

  @Test
  public void testByteAlt() {
    String[] args = {"-byte=-AB"};
    Flags.parse(args);
    assertEquals((byte) -0xAB, flagByte.get());
  }

  @Test
  public void testShortFull() {
    String[] args = {"--flagShort=2"};
    Flags.parse(args);
    assertEquals((short) 2, flagShort.get());
  }

  @Test
  public void testShortAlt() {
    String[] args = {"-short=-2"};
    Flags.parse(args);
    assertEquals((short) -2, flagShort.get());
  }

  @Test
  public void testListFull() {
    String[] args = {"--flagList=4,6,7,23,-4"};
    Flags.parse(args);
    List<Integer> list = Lists.newArrayList(4, 6, 7, 23, -4);
    assertEquals(list, flagList.get());
  }

  @Test
  public void testListAlt() {
    String[] args = {"-list=4,6,7,-4"};
    Flags.parse(args);
    List<Integer> list = Lists.newArrayList(4, 6, 7, -4);
    assertEquals(list, flagList.get());
  }

  @Test
  public void testSetFull() {
    String[] args = {"--flagSet=foo,cheese,bar,foo,bar,bam"};
    Flags.parse(args);
    Set<String> set = Sets.newHashSet("foo", "cheese", "bar", "bam");
    assertEquals(set, flagSet.get());
  }

  @Test
  public void testSetAlt() {
    String[] args = {"-set=foo,cheese"};
    Flags.parse(args);
    Set<String> set = Sets.newHashSet("foo", "cheese");
    assertEquals(set, flagSet.get());
  }

  @Test
  public void testMapFull() {
    String[] args = {"--flagMap=\"foo:4 cheese:5 bar:6 bam:7 foo:8\""};
    Flags.parse(args);
    Map<String, Integer> map = Maps.newHashMap();
    map.put("foo", 8);
    map.put("cheese", 5);
    map.put("bar", 6);
    map.put("bam", 7);
    assertEquals(map, flagMap.get());
  }

  @Test
  public void testMapAlt() {
    String[] args = {"-map=\"foo:4 cheese:5 bar:6 bam:7 foo:8 dang:9\""};
    Flags.parse(args);
    Map<String, Integer> map = Maps.newHashMap();
    map.put("foo", 8);
    map.put("cheese", 5);
    map.put("bar", 6);
    map.put("bam", 7);
    map.put("dang", 9);
    assertEquals(map, flagMap.get());
  }

  @Test
  public void testEnumFull() {
    String[] args = {"--flagEnum=TUESDAY"};
    Flags.parse(args);
    assertEquals(Day.TUESDAY, flagEnum.get());
  }

  @Test
  public void testEnumAlt() {
    String[] args = {"-enum=SATURDAY"};
    Flags.parse(args);
    assertEquals(Day.SATURDAY, flagEnum.get());
  }

  @Test
  public void testSetEnumFull() {
    String[] args = {"--flagSetEnum=TUESDAY,WEDNESDAY,THURSDAY"};
    Flags.parse(args);
    Set<Day> set = Sets.newHashSet(Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY);
    assertEquals(set, flagSetEnum.get());
  }

  @Test
  public void testSetEnumAlt() {
    String[] args = {"--flagSetEnum=MONDAY,SATURDAY"};
    Flags.parse(args);
    Set<Day> set = Sets.newHashSet(Day.MONDAY, Day.SATURDAY);
    assertEquals(set, flagSetEnum.get());
  }
}
