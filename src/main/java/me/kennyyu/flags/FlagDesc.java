package me.kennyyu.flags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking {@link Flag} objects as active flags. When
 * {@link Flags#parse(String[])} is called, all {@link Flag} fields with this
 * annotation will be scanned, and the flags will be updated to reflect the
 * values provided at the command line.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlagDesc {

  /**
   * Help information on how to use this flag.
   */
  String help();

  /**
   * Alternate name for flag. The default name will be the name of the variable
   * and command line values must be provided in the format:
   *     "--defaultFlagName=value"
   * For alternate names, only a single dash is required:
   *     "-altFlagName=value".
   */
  String altName() default "";
}