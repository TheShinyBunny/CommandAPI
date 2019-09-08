package com.shinybunny.cmdapi.annotations;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.arguments.ParameterArgument;
import com.shinybunny.cmdapi.exceptions.InvalidArgumentException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Arg {

    String value();

    String desc() default "";

    class Adapter implements AnnotationAdapter<Arg> {

        @Override
        public Class<Arg> getAnnotationType() {
            return Arg.class;
        }

        @Override
        public Object process(Object value, Arg annotation, ParameterArgument arg, CommandContext ctx) throws InvalidArgumentException {
            return null;
        }

        @Override
        public void init(ParameterArgument argument, Arg arg) {
            argument.setName(arg.value());
            argument.setDescription(arg.desc());
        }
    }

}
