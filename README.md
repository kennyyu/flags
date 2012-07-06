Flag library for Java
=====================
-   Author: Kenny Yu
-   Package: `me.kennyyu.flags`

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
To pass in the value via command line, run the class with flags passed in
the format:

    $ java MyApp --maxNumThreads=5 -shortName=foo --booleanFlag ...

All classes referenced from the main class with flags will be available
as options.

Supported Flag Types
====================
Boolean flags have short hand where `--booleanFlag=true` is the same as
`--boleanFlag`.

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

Flags also support `Enum` types. Example:


    private enum Status {
      RUNNING,
      SUSPENDED,
      TERMINATED
    }

    @FlagInfo(help = "enum example")
    private static Flag<Status> status = Flags.valueOf(Status.RUNNING);

To run it:

    $ java MyApp --status=TERMINATED

Flags also support `Collection` types.

To pass in a `List`:

    @FlagInfo(help = "list example")
    private static Flag<List<Integer>> list =
        Flags.valueOf(new ArrayList<Integer>());

To run it:

    $ java MyApp --list=3,4,5,6,6,7

To pass in a `Set`:

    @FlagInfo(help = "set example")
    private static Flag<Set<String>> set =
        Flags.valueOf(new HashSet<String>());

To run it:

    $ java MyApp --set=foo,cheese,bar

To pass in a `Map`:

    @FlagInfo(help = "map example")
    private static Flag<Map<String, Integer>> map =
        Flags.valueOf(new HashMap<String, Integer>());

To run it:

    $ java MyApp --map="foo:3 bar:4 cheese:5 bam:6"

The (key,value) pairs must be passed inside double quotes in the form
key:value separated by spaces.

To parse the flags from the command line, use `Flags.parse`, or
use `Flags.parseWithExceptions` to force catching checked
exceptions.