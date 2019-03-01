package com.shinybunny.cmdapi.exceptions;

import com.shinybunny.cmdapi.CommandAPI;
import com.shinybunny.cmdapi.Sender;

/**
 * An Exception thrown when parsing a command input, and no command found with the alias used.<br/>
 * For example, if there is no command registered with the name <code>hello</code>, and a given command looks like:<br/>
 * <pre>
 *     hello 12 true
 * </pre>
 * As "hello" is the alias used and there is no such command, this exception is thrown by {@link CommandAPI#parse(Sender, String)}
 */
public class UnknownCommandException extends Exception {
    private final String alias;

    public UnknownCommandException(String cmd) {
        super("Unknown command '" + cmd + "'!");
        this.alias = cmd;
    }

    public String getAlias() {
        return alias;
    }
}
