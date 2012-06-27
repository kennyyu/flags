package me.kennyyu.flags;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * TODO(kennyyu) Add different exceptions (add exception for Flag<?>)
 * TODO(kennyyu) add tests
 * TODO(kennyyu) add Flag<List<Integer>> support
 * TODO(kennyyu) add javadoc
 * TODO(kennyyu) quotes --foo="jack doe"
 * TODO(kennyyu) -short forms
 *
 * @author kennyyu
 */
public final class Flags {

  @FlagDesc(help = "display this help menu")
  private static Flag<Boolean> help = Flags.valueOf(false);

  public static <T> Flag<T> valueOf(T flagValue) {
    return new FlagImpl<T>(flagValue);
  }

  private static class FlagImpl<T> implements Flag<T> {
    private final T value;

    public FlagImpl(T value) {
      this.value = value;
    }

    public T get() {
      return value;
    }
  }

  public static void parse(String[] args) {
    Set<Field> fields = getAnnotatedFields();
    Map<String, String> helpMap = makeHelpMap(fields);
    Map<String, String> altNameToFullNameMap = makeAltNameToFullNameMap(fields);
    Set<String> allFieldsNameSet = makeAllFieldsNameSet(fields);
    Map<String, String> providedFieldValuesMap =
        makeProvidedFieldValuesMap(args, allFieldsNameSet, altNameToFullNameMap);
    setFieldValues(fields, providedFieldValuesMap);

    // print help message if --help was passed in, and exit
    if (help.get()) {
      printHelp(helpMap);
    }
  }

  private static Set<Field> getAnnotatedFields() {
    Reflections reflections = new Reflections(
        new ConfigurationBuilder()
          .setUrls(ClasspathHelper.forJavaClassPath())
          .setScanners(new TypeAnnotationsScanner(),
                       new TypesScanner(),
                       new FieldAnnotationsScanner()));
    Set<Field> fields = reflections.getFieldsAnnotatedWith(FlagDesc.class);

    // set accessible to true so that we can access private fields
    for (Field field : fields) {
      field.setAccessible(true);
    }
    return fields;
  }

  private static Map<String, String> makeHelpMap(Set<Field> fields) {
    Map<String, String> helpMap = new HashMap<String, String>();
    for (Field field : fields) {
      FlagDesc flagDescription = field.getAnnotation(FlagDesc.class);
      String combinedFlagNames = flagDescription.altName().equals("")
          ? field.getName()
          : field.getName() + ", " + flagDescription.altName();
      helpMap.put(combinedFlagNames, flagDescription.help() + " [from "
          + field.getDeclaringClass().getName() + "]");
    }
    return helpMap;
  }

  private static Map<String, String> makeAltNameToFullNameMap(Set<Field> fields) {
    Map<String, String> altNameToFullNameMap = new HashMap<String, String>();
    for (Field field : fields) {
      FlagDesc flagDescription = field.getAnnotation(FlagDesc.class);
      if (!flagDescription.altName().equals("")) {
        altNameToFullNameMap.put(flagDescription.altName(), field.getName());
      }
      altNameToFullNameMap.put(field.getName(), field.getName());
    }
    return altNameToFullNameMap;
  }

  private static Set<String> makeAllFieldsNameSet(Set<Field> fields) {
    Set<String> allFieldsNameSet = new HashSet<String>();
    for (Field field : fields) {
      FlagDesc flagDescription = field.getAnnotation(FlagDesc.class);
      if (!flagDescription.altName().equals("")) {
        allFieldsNameSet.add(flagDescription.altName());
      }
      allFieldsNameSet.add(field.getName());
    }
    return allFieldsNameSet;
  }

  private static Map<String, String> makeProvidedFieldValuesMap(
      String[] args,
      Set<String> allFieldsNameSet,
      Map<String, String> altNameToFullNameMap) {
    Map<String, String> providedFieldValuesMap = new HashMap<String, String>();
    for (String arg : args) {
      if (!arg.startsWith("--")) {
        continue;
      }

      // parse out --flag=value
      String flagName = arg.substring(2);
      String value = "";
      int equalsIndex = arg.indexOf("=");
      if ((equalsIndex) >= 2) {
        flagName = arg.substring(2, equalsIndex);
        value = arg.substring(equalsIndex + 1);
      }

      // throw exception if the flag is not recognized
      if (!allFieldsNameSet.contains(flagName)) {
        throw new IllegalArgumentException("unknown flag: " + flagName);
      }
      String fullName = altNameToFullNameMap.get(flagName);
      providedFieldValuesMap.put(fullName, value);
    }
    return providedFieldValuesMap;
  }

  private static <T> Flag<T> parseString(String value, Class<T> parsingClass) {
    if (parsingClass.equals(Byte.class))
      return Flags.valueOf(parsingClass.cast(Byte.parseByte(value)));
    if (parsingClass.equals(Short.class))
      return Flags.valueOf(parsingClass.cast(Short.parseShort(value)));
    if (parsingClass.equals(Integer.class))
      return Flags.valueOf(parsingClass.cast(Integer.parseInt(value)));
    if (parsingClass.equals(Long.class))
      return Flags.valueOf(parsingClass.cast(Long.parseLong(value)));
    if (parsingClass.equals(Float.class))
      return Flags.valueOf(parsingClass.cast(Float.parseFloat(value)));
    if (parsingClass.equals(Double.class))
      return Flags.valueOf(parsingClass.cast(Double.parseDouble(value)));
    if (parsingClass.equals(Boolean.class)) {
      // handle special case where booleans don't require equal signs
      if (value.equals("")) {
        return Flags.valueOf(parsingClass.cast(new Boolean(true)));
      }
      return Flags.valueOf(parsingClass.cast(Boolean.parseBoolean(value)));
    }
    if (parsingClass.equals(Character.class))
      return Flags.valueOf(parsingClass.cast(value.charAt(0)));
    return Flags.valueOf(parsingClass.cast(value));
  }

  private static void setFieldValues(
      Set<Field> fields, Map<String, String> providedFieldValuesMap) {
    for (Field field : fields) {
      String flagValueString = providedFieldValuesMap.get(field.getName());
      if (flagValueString != null) {
        try {
          Class<?> flagTypeClass = getTypeParameterOfField(field);
          field.set(null, parseString(flagValueString, flagTypeClass));
        } catch (Exception e) {
          System.err.println("problem accessing field: " + field);
          e.printStackTrace();
          System.exit(-1);
        }
      }
    }
  }

  private static Class<?> getTypeParameterOfField(Field field) {
    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
    return (Class<?>) parameterizedType.getActualTypeArguments()[0];
  }

  private static void printHelp(Map<String, String> helpMap) {
    for (Entry<String, String> entry : helpMap.entrySet()) {
      System.out.println("--" + entry.getKey());
      System.out.println("    " + entry.getValue());
    }
    System.exit(0);
  }

}
