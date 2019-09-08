package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.utils.InputReader;

public class EnumAdapter implements ArgumentAdapter<Enum> {
    /**
     * @return The class type of the parameter
     */
    @Override
    public Class<Enum> getType() {
        return Enum.class;
    }

    /**
     * Parse the input passed from the user to resolve the value for the argument.
     *
     * @param reader The input reader that is passed through the whole command string to read through the arguments.
     *               After every call to this method, the input reader {@linkplain InputReader#skipSpace() skips any spaces} before the next argument.
     * @param arg    The argument using this adapter.
     * @param ctx    The current command execution context.
     * @return The value to pass to the command's method.
     * @throws CommandParseException If the parsing fails for some reason.
     */
    @Override
    public Enum parse(InputReader reader, Argument arg, CommandContext ctx) throws CommandParseException {
        String name = reader.readWord();
        try {
            return Enum.valueOf((Class<? extends Enum>)arg.getType(),name);
        } catch (Exception e) {
            throw new CommandParseException("Invalid " + arg.getType().getSimpleName() + ": " + name);
        }
    }
}
