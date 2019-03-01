package com.shinybunny.cmdapi.annotations;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.arguments.Argument;
import com.shinybunny.cmdapi.exceptions.InvalidArgumentException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Optional {

    class Adapter implements AnnotationAdapter<Optional> {

        @Override
        public Class<Optional> getAnnotationType() {
            return Optional.class;
        }

        @Override
        public Object process(Object value, Optional annotation, Argument arg, CommandContext ctx) throws InvalidArgumentException {
            return null;
        }

        @Override
        public void init(Argument argument, Optional optional) throws Exception {
            argument.setRequired(false);
        }
    }

}
