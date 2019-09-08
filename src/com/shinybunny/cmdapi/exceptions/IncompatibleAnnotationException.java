package com.shinybunny.cmdapi.exceptions;

import com.shinybunny.cmdapi.annotations.AnnotationAdapter;
import com.shinybunny.cmdapi.arguments.Argument;

import java.lang.annotation.Annotation;

public class IncompatibleAnnotationException extends Exception {


    private final Class<? extends Annotation> first;
    private final Class<? extends Annotation> second;
    private final Argument argument;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *  @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param first
     * @param second
     * @param argument
     */
    public IncompatibleAnnotationException(String message, Class<? extends Annotation> first, Class<? extends Annotation> second, Argument argument) {
        super(message);
        this.first = first;
        this.second = second;
        this.argument = argument;
    }

    public Class<? extends Annotation> getFirst() {
        return first;
    }

    public Class<? extends Annotation> getSecond() {
        return second;
    }

    public Argument getArgument() {
        return argument;
    }
}
