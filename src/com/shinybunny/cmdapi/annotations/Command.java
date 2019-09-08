package com.shinybunny.cmdapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Command {

    String DEFAULT_NO_PERMS_MESSAGE = "You have no permissions to use that command!";

    String desc() default "";

    String[] aliases() default {};

    String permission() default "";

    String name() default "";

    String noPermissionMessage() default DEFAULT_NO_PERMS_MESSAGE;

}
