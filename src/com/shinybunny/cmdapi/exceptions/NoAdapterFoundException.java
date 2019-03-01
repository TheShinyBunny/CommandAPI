package com.shinybunny.cmdapi.exceptions;

public class NoAdapterFoundException extends Exception {

    private Class<?> type;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NoAdapterFoundException(String message, Class<?> type) {
        super(message);
        this.type = type;
    }

    public Class<?> getArgumentType() {
        return type;
    }
}
