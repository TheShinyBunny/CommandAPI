package com.shinybunny.cmdapi.annotations;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.arguments.Argument;
import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
import com.shinybunny.cmdapi.exceptions.InvalidArgumentException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Range {

    double min() default Integer.MIN_VALUE;
    double max() default Integer.MAX_VALUE;

    class Adapter implements AnnotationAdapter<Range> {

        @Override
        public Class<Range> getAnnotationType() {
            return Range.class;
        }

        @Override
        public Object process(Object value, Range annotation, Argument arg, CommandContext ctx) throws InvalidArgumentException {
            Number n = (Number)value;
            if (n.doubleValue() < annotation.min() || n.doubleValue() > annotation.max())
                throw new InvalidArgumentException(arg,arg.getName() + " value must be " + (annotation.max() < Integer.MAX_VALUE ? (annotation.min() > Integer.MIN_VALUE ? "between " + annotation.min() + " and " + annotation.max() + "!" : " smaller than " + annotation.max() + "!") : "greater than " + annotation.min() + "!"));
            return value;
        }

        @Override
        public void init(Argument argument, Range range) throws IncompatibleAnnotationException {
            if (!Number.class.isAssignableFrom(argument.getType()))
                throw new IncompatibleAnnotationException("@Range can only be applied to numbers!",range,this,argument);
        }
    }

}
