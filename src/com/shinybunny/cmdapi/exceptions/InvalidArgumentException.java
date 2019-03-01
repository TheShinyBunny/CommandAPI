package com.shinybunny.cmdapi.exceptions;

import com.shinybunny.cmdapi.arguments.Argument;

public class InvalidArgumentException extends Exception {

    private final Argument argument;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidArgumentException(Argument argument, String message) {
        super(message);
        this.argument = argument;
    }

    public Argument getArgument() {
        return argument;
    }
}
