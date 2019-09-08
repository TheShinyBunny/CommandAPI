package com.shinybunny.cmdapi.annotations;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.arguments.ParameterArgument;
import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
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
        public Object process(Object value, Optional annotation, ParameterArgument arg, CommandContext ctx) throws InvalidArgumentException {
            return null;
        }

        @Override
        public void init(ParameterArgument argument, Optional optional) throws IncompatibleAnnotationException {
            argument.incompatibleAnnotations(Optional.class,Default.class);
            argument.setRequired(false);
        }


    }

}
