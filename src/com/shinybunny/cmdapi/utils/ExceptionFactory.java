package com.shinybunny.cmdapi.utils;

import com.shinybunny.cmdapi.exceptions.CommandParseException;

public class ExceptionFactory {

    private String message;

    public ExceptionFactory(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public CommandParseException create() {
        return new CommandParseException(message);
    }
}
