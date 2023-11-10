package com.github.pareronia.aoc.solution;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Sample {

    String method();
    String input();
    String expected();
    boolean debug() default true;
}
