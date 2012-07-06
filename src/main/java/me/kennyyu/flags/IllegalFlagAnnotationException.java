package me.kennyyu.flags;

import java.lang.reflect.Field;

public class IllegalFlagAnnotationException extends FlagException {

  private static final long serialVersionUID = -7902185201659579193L;

  public IllegalFlagAnnotationException(Field field) {
    super("field: " + field.toGenericString()
        + " from " + field.getDeclaringClass() + " cannot be annotated with "
        + FlagInfo.class);
  }

}
