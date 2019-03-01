package com.shinybunny.cmdapi.exceptions;

import com.shinybunny.cmdapi.arguments.Argument;

public class MissingArgumentException extends Exception {
    private final Argument argument;

    public MissingArgumentException(Argument arg) {
        super("Missing argument " + arg);
        this.argument = arg;
    }

    public Argument getArgument() {
        return argument;
    }
}
