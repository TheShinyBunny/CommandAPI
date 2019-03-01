package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.arguments.Argument;
import com.shinybunny.cmdapi.exceptions.CommandExecutionException;
import com.shinybunny.cmdapi.utils.CommandResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ParseResults {

    private CommandContext ctx;
    private List<Entry> values;

    public ParseResults(CommandContext ctx, List<Entry> values) {
        this.ctx = ctx;
        this.values = values;
    }

    /**
     * Executes the command using these parse results.
     * @return The {@link CommandResult} object returned or inferred from the command's method.
     * @throws CommandExecutionException when, by any reason, executing the command raised an exception. This could be from {@link Method#invoke(Object, Object...) invoking the method}
     * or warping any exception thrown from the method itself (e.g. {@link InvocationTargetException})
     */
    public CommandResult execute() throws CommandExecutionException {
        return ctx.getCommand().run(this);
    }

    public Object get(Argument arg) {
        for (Entry e : values) {
            if (e.arg.equals(arg)) return e.value;
        }
        return null;
    }

    public static class Entry {
        private Argument arg;
        private Object value;

        public Entry(Argument arg, Object value) {
            this.arg = arg;
            this.value = value;
        }

        public Argument getArgument() {
            return arg;
        }

        public Object getValue() {
            return value;
        }
    }

}
