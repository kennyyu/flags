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
  String help();
  String altName() default "";
}