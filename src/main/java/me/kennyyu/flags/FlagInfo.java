package me.kennyyu.flags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking {@link Flag} objects as active flags. When
 * {@link Flags#parse(String[], String[])} is called, all {@link Flag} fields
 * with this annotation will be scanned, and the flags will be updated to
 * reflect the values provided at the command line.
 *
 * @author kennyyu (Kenny Yu)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlagInfo {

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

  /**
   * Environment for a flag. Use {@link Flags#parse(String[], String[])} or
   * {@link Flags#parseWithExceptions(String[], String[])} to load
   * different environments. Use different environments e.g. "testing",
   * "production", or "staging" to enable different flags in specific
   * environments.
   */
  String environment() default "";
}