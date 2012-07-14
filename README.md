Flag Command Line Library for Java
==================================
-   Author: Kenny Yu
-   Package: `me.kennyyu.flags`

Description
===========
This library is a simple lightweight framework for easily creating and using command
line flags. All it takes is a simple annotation with `@FlagInfo` and an invocation to
`Flags.parse` to create and use a flag! The library uses the Reflections library
to scan `Flag` objects at runtime.

This library supports parsing primitive wrapper class types, `java.util.Collection`
types such as `List`, `Set`, and `Map`, and enumerations.

Dependencies
============
-   Guava 12.0
-   Reflections 0.9.8
-   JUnit 4.0

How to Install
==============

First, clone this repository:

    $ git clone git://github.com/kennyyu/flags.git

To install the library, you must have maven installed.

## For Maven Users

Next, `cd` into the directory and build the maven package:

    $ cd flags
    flags$ mvn package

This should compile the package and execute all the tests. Finally, install the package:

    flags$ mvn install

To use this flag library, include this in the `<dependencies>` section of your `pom.xml`:

    <dependencies>
      ...
      <dependency>
        <groupId>me.kennyyu</groupId>
        <artifactId>flags</artifactId>
        <version>1.0</version>
      </dependency>
      ...
    </dependencies>

## Manual Library Install

If you would rather include the jar manually, first build the maven package with all of
the necessary dependencies:

    $ cd flags
    flags$ mvn assembly:single

This should create a `flags-1.0-jar-with-dependencies.jar` inside the `target` directory:

    flags$ ls target
    flags-1.0-jar-with-dependencies.jar ...

Include the jar in your classpath when compiling your application.

How to Create a Flag
====================
To create a new flag, create a new static `me.kennyyu.flags.Flag` field.
The parameter type of the field will be the type of the flag. Then annotate
the field with `me.kennyyu.flags.FlagInfo` and provide the necessary fields.
Example:

    @FlagInfo(help = "maximum number of threads to use", altName = "n")
    private static Flag<Integer> maxNumThreads = Flag<Integer>.valueOf(4);

This example declares a new flag indicating the maximum number of threads
to use. On the right hand side, you may provide a default value for the flag.

`me.kennyyu.flags.FlagInfo` takes several parameters:
-   `String help` : (required) help message for this flag
-   `String altName` : (optional) short name for this flag. Only one dash is needed when using the alternate name.
-   `String environment` : (optional) environment of this flag. Different flag environments may be loaded for different use cases. The default environment is the empty
string and will be loaded by default when no environment is provided to `Flags.parse`.


Parsing Command Line Arguments
==============================
Once you have defined the flag, you must call either `Flags.parse(String, String...)` or
`Flags.parseWithExceptions(String, String...)`. The difference between these two is that
`Flags.parse` may throw an unchecked `RuntimeException` whereas `Flags.parseWithExceptions`
forces you to deal with the checked exception. Here's a complete example:

    import me.kennyyu.flags.Flag;
    import me.kennyyu.flags.Flags;

    import java.util.List;

    public class MyApp {
      @FlagInfo(help = "maximum number of threads to use", altName = "n")
      private static Flag<Integer> maxNumThreads = Flags.valueOf(3);

      @FlagInfo(help = "use real logger", altName = "r")
      private static Flag<Boolean> useRealLogger = Flags.valueOf(false);

      @FlagInfo(help = "input list", altName = "l")
      private static Flag<List<String>> inputList = Flags.valueOf(new ArrayList<String>());

      public static void main(String[] args) {
        Flags.parse(args);
        System.out.println(maxNumThreads.get());
        System.out.println(useRealLogger.get());
        System.out.println(inputList.get());
      }
    }

Providing Command Line Arguments
================================
To pass in the value via command line, run the class with flags passed in
the format:

    $ java MyApp --maxNumThreads=5 --useRealLogger -l=foo,bar,baz

All classes referenced from the main class with flags will be available
as options. If `--help` or `-h` is passed in, then a help menu will be printed
with all available flag options, and the JVM will exit with a 0 exit status.

Loading Different Environments
==============================
Loading different environments is especially useful when we wish to separate testing
flags from production flags: testing flags should never be allowed in a production
environment.

For example, if we have a flag defined by:

    @FlagInfo(help = "use mocking libraries", altName = "mock", environment = "test")
    private static Flag<Boolean> useMockLibraries = Flags.valueOf(false);

Then we can choose to load the test environment in `main` with:

    public static void main(String[] args) {
      Flags.parse(args, "test");
    }

If no environment is provided in `FlagInfo`, then the empty string will be used as the default environment.
If no environment is provided to `Flags.parse` or `Flags.parseWithException`, then the default environment
will be loaded.

Supported Flag Types
====================

## Primitive Wrapper Classes
The currently supported types for flags include wrapper classes:
-   Integer
-   Long
-   Short
-   Boolean
-   Double
-   Float
-   Character
-   String
-   Byte

Boolean flags have short hand where `--booleanFlag=true` is the same as
`--boleanFlag`.

## Enumerations
Flags also support enumeration types. Example:


    private enum Status {
      RUNNING,
      SUSPENDED,
      TERMINATED
    }

    @FlagInfo(help = "enum example")
    private static Flag<Status> status = Flags.valueOf(Status.RUNNING);

To run it:

    $ java MyApp --status=TERMINATED

## Collections
Flags also support `java.util.Collection` types.

### List
To pass in a `java.util.List`:

    @FlagInfo(help = "list example")
    private static Flag<List<Integer>> list = Flags.valueOf(new ArrayList<Integer>());

To run it:

    $ java MyApp --list=3,4,5,6,6,7

### Set
To pass in a `java.util.Set`:

    @FlagInfo(help = "set example")
    private static Flag<Set<String>> set = Flags.valueOf(new HashSet<String>());

To run it:

    $ java MyApp --set=foo,cheese,bar

### Map
To pass in a `java.util.Map`:

    @FlagInfo(help = "map example")
    private static Flag<Map<String, Integer>> map = Flags.valueOf(new HashMap<String, Integer>());

To run it:

    $ java MyApp --map="foo:3 bar:4 cheese:5 bam:6"

The (key,value) pairs must be passed inside double quotes in the form
key:value separated by spaces.