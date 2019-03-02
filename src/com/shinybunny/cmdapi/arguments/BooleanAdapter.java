package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.utils.InputReader;

public class BooleanAdapter implements ArgumentAdapter<Boolean> {
    @Override
    public Class<Boolean> getType() {
        return Boolean.TYPE;
    }

    @Override
    public Boolean parse(InputReader reader, Argument arg, CommandContext ctx) throws CommandParseException {
        return reader.readBoolean();
    }
}
