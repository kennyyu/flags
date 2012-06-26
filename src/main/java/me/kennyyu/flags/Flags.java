package me.kennyyu.flags;

import java.lang.reflect.Field;
import java.lang.reflect.TypeVariable;
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
import org.reflections.util.FilterBuilder;

public final class Flags {

    public static <T> Flag<T> value(T flagValue) {
        return new FlagImpl<T>(flagValue);
    }

    public static void parse(String[] args) {
        // get all fields with the declared annotation
        Reflections reflections = new Reflections(
            new ConfigurationBuilder()
                .filterInputsBy(new FilterBuilder().include("me.kennyyu.flags"))
                .setUrls(ClasspathHelper.forClassLoader())
                .setScanners(new TypeAnnotationsScanner(), 
                             new TypesScanner(),
                             new FieldAnnotationsScanner()));
        Set<Field> fields = reflections.getFieldsAnnotatedWith(FlagDesc.class);
        
        // construct the set of possible flags and help message
        Map<String, String> fieldHelp = new HashMap<String, String>();
        Set<String> fieldNameSet = new HashSet<String>();
        for (Field field : fields) {
            FlagDesc flagDescription = field.getAnnotation(FlagDesc.class);
            fieldNameSet.add(flagDescription.name());
            fieldHelp.put(flagDescription.name(), flagDescription.help()
                    + " [from " + field.getDeclaringClass().getName() + "]");
        }

        // parse command line arguments into flags
        Map<String, String> mapFieldNamesToValues = new HashMap<String, String>();
        for (String arg : args) {
            if (!arg.startsWith("--")) {
                throw new IllegalArgumentException("unknowin string: " + arg);
            }
            String flagName = arg.substring(2);
            String value = "";
            int equalsIndex = arg.indexOf("=");
            if ((equalsIndex) >= 2) {
                flagName = arg.substring(2,equalsIndex);
                value = arg.substring(equalsIndex + 1);
            }
            if (!fieldNameSet.contains(flagName)) {
                throw new IllegalArgumentException("unknown flag: " + flagName);
            }
            mapFieldNamesToValues.put(flagName, value);
        }

        // print help message if --help was passed in
        if (mapFieldNamesToValues.containsKey("help")) {
            System.out.println("--help");
            System.out.println("    print out this error message");
            for (Entry<String, String> entry : fieldHelp.entrySet()) {
                System.out.println("--" + entry.getKey());
                System.out.println("    " + entry.getValue());
            }
            System.exit(0);
        }

        // set the value of the flags fields
        for (Field field : fields) {
            FlagDesc flagDescription = field.getAnnotation(FlagDesc.class);
            String flagValueString = mapFieldNamesToValues.get(flagDescription.name());
            if (flagValueString != null) {
                @SuppressWarnings("unchecked")
                Class<Class<?>> flagClass = (Class<Class<?>>) field.getType();
                TypeVariable<Class<Class<?>>>[] flagValueType = flagClass.getTypeParameters();
                try {
                    // TODO(kennyyu) figure out how to set the value of the flag
                    field.set(null, parseString(flagValueString, flagValueType[0].getGenericDeclaration()));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    private static <T> T parseString(String value, Class<T> parsingClass) {
        if (parsingClass.equals(Byte.class))
            return parsingClass.cast(Byte.parseByte(value));
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
        if (parsingClass.equals(Boolean.class))
            return parsingClass.cast(Boolean.parseBoolean(value));
        if (parsingClass.equals(Character.class))
            return parsingClass.cast(value.charAt(0));
        return parsingClass.cast(value);
    }

}
