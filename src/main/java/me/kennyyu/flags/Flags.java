package me.kennyyu.flags;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO(kennyyu) add javadoc about exceptions
 */
public final class Flags {

  @FlagDesc(help = "display this help menu", altName="h")
  private static Flag<Boolean> help = Flags.valueOf(false);

  /**
   * Create a {@link Flag} with the given value.
   */
  public static <T> Flag<T> valueOf(T flagValue) {
    return new FlagImpl<T>(flagValue);
  }

  /**
   * Create a {@link Flag} holding a {@link List} of values.
   */
  public static <T> Flag<List<T>> valueOf(List<T> flagValue) {
    return new FlagImpl<List<T>>(flagValue);
  }

  /**
   * Create a {@link Flag} holding a {@link Set} of values.
   */
  public static <T> Flag<Set<T>> valueOf(Set<T> flagValue) {
    return new FlagImpl<Set<T>>(flagValue);
  }

  /**
   * Create a {@link Flag} holding a {@link Map} of values.
   */
  public static <K, V> Flag<Map<K, V>> valueOf(Map<K, V> flagValue) {
    return new FlagImpl<Map<K, V>>(flagValue);
  }

  /**
   * Private implementation of {@link Flag}.
   */
  private static class FlagImpl<T> implements Flag<T> {
    private final T value;

    public FlagImpl(T value) {
      this.value = value;
    }

    @Override
    public T get() {
      return value;
    }
  }

  /**
   * Parses the command line arguments and updates as necessary all {@link Flag}
   * objects annotated with {@link FlagDesc}.
   *
   * If "--help" of "-h" is passed in at the command line, then the help menu
   * will be printed and the program will terminate.
   *
   * @param args command line arguments in the form
   *     "--defaultFlagName=value --booleanFlag -c=foo ..."
   */
  public static void parse(String[] args) {
    Set<Field> fields = getAnnotatedFields();
    ensureAnnotatedFieldsAreFlags(fields);

    Set<Field> inacessibleFields = makeFieldsAccessible(fields);
    Map<String, String> helpMap = makeHelpMap(fields);
    Map<String, String> altNameToFullNameMap = makeAltNameToFullNameMap(fields);
    Set<String> allFieldsNameSet = makeAllFieldsNameSet(fields);
    Map<String, String> providedFieldValuesMap = makeProvidedFieldValuesMap(
        args, allFieldsNameSet, altNameToFullNameMap);
    setFieldValues(fields, providedFieldValuesMap);

    for (Field field : inacessibleFields) {
      field.setAccessible(false);
    }

    if (help.get()) {
      printHelp(helpMap);
    }
  }

  /**
   * Ensures that all fields are Flag objects
   * @param fields
   * @throws IllegalFlagAnnotationException if any field is not a Flag
   *     object.
   */
  private static void ensureAnnotatedFieldsAreFlags(Set<Field> fields) {
    for (Field field : fields) {
      if (!field.getType().equals(Flag.class))
        throw new IllegalFlagAnnotationException(field);
    }
  }

  /**
   * @return all {@link Field} objects annotated with {@link FlagDesc}.
   */
  private static Set<Field> getAnnotatedFields() {
    Reflections reflections = new Reflections(
        new ConfigurationBuilder()
          .setUrls(ClasspathHelper.forJavaClassPath())
          .setScanners(
              new TypeAnnotationsScanner(),
              new TypesScanner(),
              new FieldAnnotationsScanner()));
    return reflections.getFieldsAnnotatedWith(FlagDesc.class);
  }

  /**
   * Retrieve all fields that are inaccessible from this class and marks them
   * as accessible.
   * @return the set of fields that were inaccessible.
   */
  private static Set<Field> makeFieldsAccessible(Set<Field> fields) {
    Set<Field> inaccessibleFields = Sets.newHashSet();
    for (Field field : fields) {
      if (!field.isAccessible()) {
        field.setAccessible(true);
        inaccessibleFields.add(field);
      }
    }
    return inaccessibleFields;
  }

  /**
   * @return {@link Map} (flag name) -> (flag information).
   */
  private static Map<String, String> makeHelpMap(Set<Field> fields) {
    Map<String, String> helpMap = Maps.newTreeMap();
    for (Field field : fields) {
      FlagDesc flagDescription = field.getAnnotation(FlagDesc.class);
      String combinedFlagNames = flagDescription.altName().equals("")
          ? field.getName()
          : field.getName() + ", -" + flagDescription.altName();
      helpMap.put("--" + combinedFlagNames, flagDescription.help() + " [from "
          + field.getDeclaringClass().getName() + "]");
    }
    return helpMap;
  }

  /**
   * @return {@link Map} from (flag name or alternate name) -> (flag name).
   */
  private static Map<String, String> makeAltNameToFullNameMap(
      Set<Field> fields) {
    Map<String, String> altNameToFullNameMap = Maps.newHashMap();
    for (Field field : fields) {
      FlagDesc flagDescription = field.getAnnotation(FlagDesc.class);
      if (!flagDescription.altName().equals("")) {
        altNameToFullNameMap.put(flagDescription.altName(), field.getName());
      }
      altNameToFullNameMap.put(field.getName(), field.getName());
    }
    return altNameToFullNameMap;
  }

  /**
   * @return {@link Set} containing all the string versions of the flag names.
   */
  private static Set<String> makeAllFieldsNameSet(Set<Field> fields) {
    Set<String> allFieldsNameSet = Sets.newHashSet();
    for (Field field : fields) {
      FlagDesc flagDescription = field.getAnnotation(FlagDesc.class);
      if (!flagDescription.altName().equals("")) {
        if (allFieldsNameSet.contains(flagDescription.altName())) {
          throw new DuplicateFlagNameException(flagDescription.altName());
        }
        allFieldsNameSet.add(flagDescription.altName());
      }
      if (allFieldsNameSet.contains(field.getName())) {
        throw new DuplicateFlagNameException(field.getName());
      }
      allFieldsNameSet.add(field.getName());
    }
    return allFieldsNameSet;
  }

  /**
   * Return a map that takes each string of the form
   *      "--flagName=stringValue"
   * and creates a map (flagName) -> (stringValue).
   *
   * @param args strings of the form "--flagName=stringValue"
   * @param allFieldsNameSet set of all possible flags as strings
   * @param altNameToFullNameMap map from all possible names of a flag to the
   *    flags canonical name
   */
  private static Map<String, String> makeProvidedFieldValuesMap(
      String[] args,
      Set<String> allFieldsNameSet,
      Map<String, String> altNameToFullNameMap) {
    Map<String, String> providedFieldValuesMap = Maps.newHashMap();
    for (String arg : args) {
      String flagName = "";
      String value = "";

      if (!arg.startsWith("-")) {
        continue; // skip this string
      } else if (arg.startsWith("--")) {
        // parse out --flag=value
        int equalsIndex = arg.indexOf("=");
        flagName = arg.substring(2);
        if ((equalsIndex) >= 2) {
          flagName = arg.substring(2, equalsIndex);
          value = arg.substring(equalsIndex + 1);
        }
      } else if (arg.startsWith("-")) {
        // parse out -f=value
        int equalsIndex = arg.indexOf("=");
        flagName = arg.substring(1);
        if ((equalsIndex) >= 1) {
          flagName = arg.substring(1, equalsIndex);
          value = arg.substring(equalsIndex + 1);
        }
      }

      // throw exception if the flag is not recognized
      if (!allFieldsNameSet.contains(flagName)) {
        throw new UnknownFlagException(flagName);
      }

      // get the flag's canonical name
      String fullName = altNameToFullNameMap.get(flagName);
      providedFieldValuesMap.put(fullName, value);
    }
    return providedFieldValuesMap;
  }

  /**
   * Convert the string to the corresponding value of the provided class
   * @param value the string to be parsed
   * @param parsingClass the class to convert the string into
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static <T> T valueOfString(String value, Class<T> parsingClass) {
    if (parsingClass.isEnum())
      return (T) Enum.valueOf((Class) parsingClass, value);
    if (parsingClass.equals(Byte.class))
      return parsingClass.cast(new Byte((byte) Integer.parseInt(value, 16)));
    if (parsingClass.equals(Short.class))
      return parsingClass.cast(Short.parseShort(value));
    if (parsingClass.equals(Integer.class))
      return parsingClass.cast(Integer.parseInt(value));
    if (parsingClass.equals(Long.class))
      return parsingClass.cast(Long.parseLong(value));
    if (parsingClass.equals(Float.class))
      return parsingClass.cast(Float.parseFloat(value));
    if (parsingClass.equals(Double.class))
      return parsingClass.cast(Double.parseDouble(value));
    if (parsingClass.equals(Boolean.class)) {
      // handle special case where booleans don't require equal signs
      // e.g. "--isLarge=true" is the same as "--isLarge"
      if (value.equals(""))
        return parsingClass.cast(new Boolean(true));
      return parsingClass.cast(Boolean.parseBoolean(value));
    }
    if (parsingClass.equals(Character.class))
      return parsingClass.cast(value.charAt(0));
    if (parsingClass.equals(String.class)) {
      return parsingClass.cast(value);
    }
    throw new UnsupportedFlagTypeException(parsingClass);
  }

  /**
   * Updates all flags to the values provided at the command line.
   */
  private static void setFieldValues(
      Set<Field> fields,
      Map<String,
      String> providedFieldValuesMap) {
    for (Field field : fields) {
      String flagValueString = providedFieldValuesMap.get(field.getName());
      if (flagValueString != null) { // check if the flag was provided
        setField(field, flagValueString);
      }
    }
  }

  /**
   * Updates field to be the corresponding value of flagValueString.
   */
  private static void setField(Field field, String flagValueString) {
    // Get the type nested inside Flag<?>
    Type parameter = ((ParameterizedType) field.getGenericType())
        .getActualTypeArguments()[0];

    // Get the parameters nested inside Flag<Collection<?>>
    if (parameter instanceof ParameterizedType) {
      Type rawType = ((ParameterizedType) parameter).getRawType();
      if (rawType.equals(List.class)) {
        Type listParameter = ((ParameterizedType) parameter)
            .getActualTypeArguments()[0];
        setListField(field, flagValueString, (Class<?>) listParameter);
      } else if (rawType.equals(Set.class)) {
        Type setParameter = ((ParameterizedType) parameter)
            .getActualTypeArguments()[0];
        setSetField(field, flagValueString, (Class<?>) setParameter);
      } else if (rawType.equals(Map.class)) {
        Type[] parameters = ((ParameterizedType) parameter)
            .getActualTypeArguments();
        setMapField(
            field,
            flagValueString,
            (Class<?>) parameters[0],
            (Class<?>) parameters[1]);
      } else {
        throw new UnsupportedFlagTypeException(parameter);
      }
    } else if (parameter instanceof Class) {
      try {
        field.set(null, Flags.valueOf(valueOfString(
            flagValueString,
            (Class<?>) parameter)));
      } catch (Exception e) {
        throw new FlagException(e);
      }
    } else {
      throw new UnsupportedFlagTypeException(parameter);
    }
  }

  /**
   * Updates field to be a {@link List} containing the values in flagValueString
   * @param field the field to update
   * @param flagValueString comma separated list of values in this list
   * @param parameterType the type nested in this list
   */
  private static <T> void setListField(
      Field field,
      String flagValueString,
      Class<T> parameterType) {
    List<T> elements = Lists.newArrayList();
    String[] elementStrings = flagValueString.split(",");
    for (String elementString : elementStrings) {
      elements.add(valueOfString(elementString, parameterType));
    }
    try {
      field.set(null, Flags.valueOf(elements));
    } catch (Exception e) {
      throw new FlagException(e);
    }
  }

  /**
   * Updates field to be a {@link Set} containing the values in flagValueString
   * @param field the field to update
   * @param flagValueString comma separated list of values in this list
   * @param parameterType the type nested in this list
   */
  private static <T> void setSetField(
      Field field,
      String flagValueString,
      Class<T> parameterType) {
    Set<T> elements = Sets.newHashSet();
    String[] elementStrings = flagValueString.split(",");
    for (String elementString : elementStrings) {
      elements.add(valueOfString(elementString, parameterType));
    }
    try {
      field.set(null, Flags.valueOf(elements));
    } catch (Exception e) {
      throw new FlagException(e);
    }
  }

  /**
   * Updates field to be a {@link Map} containing the values in flagValueString
   * @param field the field to update
   * @param flagValueString string formatted in the form
   *    "key1:value1 key2:value2 ..."
   * @param keyType type of the Key
   * @param valueType type of the Value
   */
  private static <K,V> void setMapField(
      Field field,
      String flagValueString,
      Class<K> keyType,
      Class<V> valueType) {
    Map<K,V> elements = Maps.newHashMap();
    flagValueString =
        flagValueString.substring(1, flagValueString.length() - 1);
    String[] elementStrings = flagValueString.split(" ");
    for (String elementString : elementStrings) {
      String[] components = elementString.split(":");
      if (components.length != 2) {
        throw new IllegalFlagFormatException(elementString);
      }
      elements.put(
          valueOfString(components[0], keyType),
          valueOfString(components[1], valueType));
    }
    try {
      field.set(null, Flags.valueOf(elements));
    } catch (Exception e) {
      throw new FlagException(e);
    }
  }

  /**
   * Print out the help menu and quit.
   */
  private static void printHelp(Map<String, String> helpMap) {
    StringBuilder builder = new StringBuilder();
    for (Entry<String, String> entry : helpMap.entrySet()) {
      builder.append(entry.getKey() + "\n    " + entry.getValue() + "\n");
    }
    System.out.println(builder.toString());
    System.exit(0);
  }

}
