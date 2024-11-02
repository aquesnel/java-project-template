package io.github.aquesnel.utils.cli;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({TYPE})
@Inherited
public @interface ProgramName {
  String value();
}
