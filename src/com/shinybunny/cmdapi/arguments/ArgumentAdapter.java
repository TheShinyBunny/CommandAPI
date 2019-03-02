package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.exceptions.MissingArgumentException;
import com.shinybunny.cmdapi.utils.InputReader;

/**
 * An argument adapter is used for parsing a type of parameter from a string.
 * @param <T> the type of parameter this adapter should be used for
 */
public interface ArgumentAdapter<T> {

    /**
     * @return The class type of the parameter
     */
    Class<T> getType();

    /**
     * Parse the input passed from the user to get, create, resolve or find the value of the responsive type.
     * @param reader The input reader that is passed through the whole command string to read through the arguments.
     *               After every call to this method, the input reader {@linkplain InputReader#skipSpace() skips all spaces} for the next argument.
     * @param arg The argument using this adapter.
     * @param ctx The current command execution context.
     * @return The value to pass to the command's method.
     * @throws CommandParseException If the parsing fails for some reason.
     */
    T parse(InputReader reader, Argument arg, CommandContext ctx) throws CommandParseException;

    /**
     * Whether this adapter allows null values to be returned by {@link #parse(InputReader, Argument, CommandContext)}.
     * @return true to NOT throw a {@link MissingArgumentException} when a null value is returned.
     */
    default boolean nullable() {
        return false;
    }

    default Object outOfSyntax(CommandContext ctx) {
        return null;
    }

    /**
     * @return Get the default value for this adapter. Useful for primitive types and any type that has a default type.
     */
    default T getDefault() {
        return null;
    }

    default String toString(Argument arg) {
        return arg.getType().getSimpleName().toLowerCase();
    }
}
