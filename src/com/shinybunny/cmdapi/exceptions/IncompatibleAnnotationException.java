package com.shinybunny.cmdapi.exceptions;

import com.shinybunny.cmdapi.annotations.AnnotationAdapter;
import com.shinybunny.cmdapi.arguments.Argument;

import java.lang.annotation.Annotation;

public class IncompatibleAnnotationException extends Exception {

    private Annotation annotation;
    private AnnotationAdapter adapter;
    private Argument argument;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param argument
     */
    public IncompatibleAnnotationException(String message, Annotation annotation, AnnotationAdapter adapter, Argument argument) {
        super(message);
        this.annotation = annotation;
        this.adapter = adapter;
        this.argument = argument;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public AnnotationAdapter getAdapter() {
        return adapter;
    }
}
