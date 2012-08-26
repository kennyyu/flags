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
public class FlagsTest {

  @FlagInfo(help = "flagInteger", altName = "int", environment = "testing")
  private static Flag<Integer> flagInteger = Flags.valueOf(0);

  @FlagInfo(help = "flagLong", altName = "long", environment = "testing")
  private static Flag<Long> flagLong = Flags.valueOf(0L);

  @FlagInfo(help = "flagDouble", altName = "double", environment = "testing")
  private static Flag<Double> flagDouble = Flags.valueOf(0.0);

  @FlagInfo(help = "flagFloat", altName = "float", environment = "testing")
  private static Flag<Float> flagFloat = Flags.valueOf((float) 0.0);

  @FlagInfo(help = "flagBoolean", altName = "boolean", environment = "testing")
  private static Flag<Boolean> flagBoolean = Flags.valueOf(false);

  @FlagInfo(help = "flagCharacter", altName = "char", environment = "testing")
  private static Flag<Character> flagCharacter = Flags.valueOf('\0');

  @FlagInfo(help = "flagString", altName = "string", environment = "testing")
  private static Flag<String> flagString = Flags.valueOf("");

  @FlagInfo(help = "flagByte", altName = "byte", environment = "testing")
  private static Flag<Byte> flagByte = Flags.valueOf((byte) 0x00);

  @FlagInfo(help = "flagAlt", altName = "short", environment = "testing")
  private static Flag<Short> flagShort = Flags.valueOf((short) 0);

  @FlagInfo(help = "flagList", altName = "list", environment = "testing")
  private static Flag<List<Integer>> flagList =
      Flags.valueOf(Lists.<Integer>newArrayList());

  @FlagInfo(help = "flagSet", altName = "set", environment = "testing")
  private static Flag<Set<String>> flagSet =
      Flags.valueOf(Sets.<String>newHashSet());

  @FlagInfo(help = "flagMap", altName = "map", environment = "testing")
  private static Flag<Map<String, Integer>> flagMap =
      Flags.valueOf(Maps.<String, Integer>newHashMap());

  @FlagInfo(help = "flagEnum", altName = "enum", environment = "testing")
  private static Flag<Day> flagEnum = Flags.valueOf(Day.SUNDAY);

  @FlagInfo(help = "flagSetEnum", altName = "setEnum", environment = "testing")
  private static Flag<Set<Day>> flagSetEnum =
      Flags.valueOf(Sets.<Day>newHashSet());

  @FlagInfo(help = "flagStaticFinal", altName = "staticFinal",
      environment = "testing")
  private static final Flag<Integer> flagStaticFinal = Flags.valueOf(0);

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
    Flags.parse(args, "testing");
    assertEquals(7, flagInteger.get());
  }

  @Test
  public void testIntegerAlt() {
    String[] args = {"-int=-5"};
    Flags.parse(args, "testing");
    assertEquals(-5, flagInteger.get());
  }

  @Test
  public void testLongFull() {
    String[] args = {"--flagLong=7"};
    Flags.parse(args, "testing");
    assertEquals(7L, flagLong.get());
  }

  @Test
  public void testLongAlt() {
    String[] args = {"-long=-5"};
    Flags.parse(args, "testing");
    assertEquals(-5L, flagLong.get());
  }

  @Test
  public void testDoubleFull() {
    String[] args = {"--flagDouble=5.6"};
    Flags.parse(args, "testing");
    assertEquals(5.6, flagDouble.get());
  }

  @Test
  public void testDoubleAlt() {
    String[] args = {"-double=-5.4"};
    Flags.parse(args, "testing");
    assertEquals(-5.4, flagDouble.get());
  }

  @Test
  public void testFloatFull() {
    String[] args = {"--flagFloat=5.6"};
    Flags.parse(args, "testing");
    assertEquals((float) 5.6, flagFloat.get());
  }

  @Test
  public void testFloatAlt() {
    String[] args = {"-float=-5.4"};
    Flags.parse(args, "testing");
    assertEquals((float) -5.4, flagFloat.get());
  }

  @Test
  public void testBooleanFull() {
    String[] args = {"--flagBoolean=true"};
    Flags.parse(args, "testing");
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanAlt() {
    String[] args = {"-boolean=true"};
    Flags.parse(args, "testing");
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanFullDefault() {
    String[] args = {"--flagBoolean"};
    Flags.parse(args, "testing");
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testBooleanAltDefault() {
    String[] args = {"-boolean"};
    Flags.parse(args, "testing");
    assertEquals(true, flagBoolean.get());
  }

  @Test
  public void testCharacterFull() {
    String[] args = {"--flagCharacter=c"};
    Flags.parse(args, "testing");
    assertEquals('c', flagCharacter.get());
  }

  @Test
  public void testCharacterAlt() {
    String[] args = {"-char=d"};
    Flags.parse(args, "testing");
    assertEquals('d', flagCharacter.get());
  }

  @Test
  public void testStringFull() {
    String[] args = {"--flagString=bar"};
    Flags.parse(args, "testing");
    assertEquals("bar", flagString.get());
  }

  @Test
  public void testStringAlt() {
    String[] args = {"-string=foo"};
    Flags.parse(args, "testing");
    assertEquals("foo", flagString.get());
  }

  @Test
  public void testByteFull() {
    String[] args = {"--flagByte=AB"};
    Flags.parse(args, "testing");
    assertEquals((byte) 0xAB, flagByte.get());
  }

  @Test
  public void testByteAlt() {
    String[] args = {"-byte=-AB"};
    Flags.parse(args, "testing");
    assertEquals((byte) -0xAB, flagByte.get());
  }

  @Test
  public void testShortFull() {
    String[] args = {"--flagShort=2"};
    Flags.parse(args, "testing");
    assertEquals((short) 2, flagShort.get());
  }

  @Test
  public void testShortAlt() {
    String[] args = {"-short=-2"};
    Flags.parse(args, "testing");
    assertEquals((short) -2, flagShort.get());
  }

  @Test
  public void testListFull() {
    String[] args = {"--flagList=4,6,7,23,-4"};
    Flags.parse(args, "testing");
    List<Integer> list = Lists.newArrayList(4, 6, 7, 23, -4);
    assertEquals(list, flagList.get());
  }

  @Test
  public void testListAlt() {
    String[] args = {"-list=4,6,7,-4"};
    Flags.parse(args, "testing");
    List<Integer> list = Lists.newArrayList(4, 6, 7, -4);
    assertEquals(list, flagList.get());
  }

  @Test
  public void testSetFull() {
    String[] args = {"--flagSet=foo,cheese,bar,foo,bar,bam"};
    Flags.parse(args, "testing");
    Set<String> set = Sets.newHashSet("foo", "cheese", "bar", "bam");
    assertEquals(set, flagSet.get());
  }

  @Test
  public void testSetAlt() {
    String[] args = {"-set=foo,cheese"};
    Flags.parse(args, "testing");
    Set<String> set = Sets.newHashSet("foo", "cheese");
    assertEquals(set, flagSet.get());
  }

  @Test
  public void testMapFull() {
    String[] args = {"--flagMap=\"foo:4 cheese:5 bar:6 bam:7 foo:8\""};
    Flags.parse(args, "testing");
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
    Flags.parse(args, "testing");
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
    Flags.parse(args, "testing");
    assertEquals(Day.TUESDAY, flagEnum.get());
  }

  @Test
  public void testEnumAlt() {
    String[] args = {"-enum=SATURDAY"};
    Flags.parse(args, "testing");
    assertEquals(Day.SATURDAY, flagEnum.get());
  }

  @Test
  public void testSetEnumFull() {
    String[] args = {"--flagSetEnum=TUESDAY,WEDNESDAY,THURSDAY"};
    Flags.parse(args, "testing");
    Set<Day> set = Sets.newHashSet(Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY);
    assertEquals(set, flagSetEnum.get());
  }

  @Test
  public void testSetEnumAlt() {
    String[] args = {"--flagSetEnum=MONDAY,SATURDAY"};
    Flags.parse(args, "testing");
    Set<Day> set = Sets.newHashSet(Day.MONDAY, Day.SATURDAY);
    assertEquals(set, flagSetEnum.get());
  }

  @Test
  public void testAll() {
    String[] args = {
        "--flagInteger=-5",
        "-long=6",
        "-double=-4.5",
        "--flagFloat=5.6",
        "--flagBoolean",
        "-char=d",
        "-string=foo",
        "-byte=BC",
        "--flagShort=2",
        "-list=4,5,6",
        "--flagSet=cheese,bar",
        "-map=\"h:5 i:10 j:6\"",
        "--flagEnum=FRIDAY",
        "--flagSetEnum=MONDAY,SUNDAY,THURSDAY"
    };
    Flags.parse(args, "testing");
    assertEquals(-5, flagInteger.get());
    assertEquals(6L, flagLong.get());
    assertEquals(-4.5, flagDouble.get());
    assertEquals((float) 5.6, flagFloat.get());
    assertEquals(true, flagBoolean.get());
    assertEquals('d', flagCharacter.get());
    assertEquals("foo", flagString.get());
    assertEquals((byte) 0xBC, flagByte.get());
    assertEquals((short) 2, flagShort.get());
    assertEquals(Lists.newArrayList(4, 5, 6), flagList.get());
    assertEquals(Sets.newHashSet("cheese", "bar"), flagSet.get());
    Map<String, Integer> map = Maps.newHashMap();
    map.put("h", 5);
    map.put("i", 10);
    map.put("j", 6);
    assertEquals(map, flagMap.get());
    assertEquals(Day.FRIDAY, flagEnum.get());
    assertEquals(
        Sets.newHashSet(Day.MONDAY, Day.SUNDAY, Day.THURSDAY),
        flagSetEnum.get());
  }

  @Test
  public void testDefault() {
    String[] args = {"--flagInteger=7"};
    Flags.parse(args, "testing");
    assertEquals(0, flagInteger.defaultValue());
  }

  @Test
  public void testStaticFinal() {
    String[] args = {"--flagStaticFinal=8"};
    Flags.parse(args, "testing");
    assertEquals(8, flagStaticFinal.get());
  }
}
