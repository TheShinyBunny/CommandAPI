package com.shinybunny.cmdapi.annotations;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.arguments.Argument;
import com.shinybunny.cmdapi.arguments.ParameterArgument;
import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
import com.shinybunny.cmdapi.exceptions.InvalidArgumentException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;
import com.shinybunny.cmdapi.utils.InputReader;

import java.lang.annotation.Annotation;

/**
 * An annotation adapter is a base interface for applying special rules and modifications to an argument.<br/>
 * When a parameter is annotated with the annotation represented by this adapter, the following stuff happen when registering the command:
 * <ol>
 *     <li>First, the system looks for a matching adapter for that annotation, and maps it to the respective adapter</li>
 *     <li>If no adapter found for an annotation, a {@link NoAdapterFoundException} is thrown</li>
 *     <li>The adapter is mapped to the {@link Annotation} instance for the argument</li>
 *     <li>If the result of {@link #isRequired(Annotation)} is false, the argument becomes not required</li>
 *     <li>If the result of {@link #isSyntax()} is false, the argument becomes not part of the command syntax</li>
 *     <li>Finally, {@link #init(ParameterArgument, Annotation)} is called, potentially throwing an {@link IncompatibleAnnotationException}
 *     in case the annotation is not compatible with the parameter type it's bound to, or another annotation in that argument.</li>
 * </ol>
 * @param <A> the annotation type
 */
public interface AnnotationAdapter<A extends Annotation> {

    /**
     * A dummy annotation adapter, used for preventing a {@link NoAdapterFoundException} for an annotation that is only used by other annotations, or annotation that has nothing to do with the command.
     * @param annotationType The annotation class to connect to
     * @return A new annotation adapter with {@link #getAnnotationType()} returning the given <code>annotationType</code>
     */
    static <A extends Annotation> AnnotationAdapter<A> dummy(Class<A> annotationType) {
        return new AnnotationAdapter<A>() {
            @Override
            public Class<A> getAnnotationType() {
                return annotationType;
            }

            @Override
            public Object process(Object value, A annotation, ParameterArgument arg, CommandContext ctx) throws InvalidArgumentException {
                return value;
            }
        };
    }

    /**
     * Whether an argument annotated with this annotation adapter's type should be treated as not part of the command syntax.
     * @return The value <code>true</code> to {@link Argument#process(Object, CommandContext) process} the argument before {@link Argument#parse(InputReader, CommandContext) parsing} it.
     */
    default boolean isSyntax() {
        return true;
    }

    /**
     * The type of annotation to connect to.
     * @return <code>MyAnnotation.class;</code>
     */
    Class<A> getAnnotationType();

    /**
     * Processes the argument value while parsing a command execution. Called with <code>value = null</code> if no value is resolved yet.
     * It's also called if either this adapter or other annotation adapter for the argument {@link #isSyntax() is not part of the syntax}.
     * @param value The currently parsed or resolved value to pass to the command's method.
     * @param annotation An instance of this adapter's annotation for the parameter
     * @param arg The argument this adapter is used to process
     * @param ctx The current command execution context
     * @return A new value to change the used value, or either null/the passed <code>value</code>, to make no changes.
     * @throws InvalidArgumentException If this annotation's purpose suggests the parsed value is invalid (not in range, too short, to long, in an invalid state, etc.
     */
    Object process(Object value, A annotation, ParameterArgument arg, CommandContext ctx) throws InvalidArgumentException;

    default boolean isRequired(A a) {
        return true;
    }

    /**
     * Called when a command is registered with a parameter annotated with this adapter's type, to modify the argument settings or to invalidate the use of this annotation.
     * @param argument The argument using this adapter
     * @param a The annotation instance attached to the method's parameter
     * @throws IncompatibleAnnotationException to invalidate the use of this annotation for that argument
     */
    default void init(ParameterArgument argument, A a) throws IncompatibleAnnotationException {

    }

}
