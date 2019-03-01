package com.shinybunny.cmdapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Settings {

    String desc() default "";

    String[] aliases() default {};

    String permission() default "";

    String name() default "";

    String noPermissionMessage() default "You have no permissions to use that command!";

}
