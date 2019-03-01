package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.Suggestions;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.utils.InputReader;

public class NumberAdapter implements ArgumentAdapter<Number> {
    @Override
    public Class<Number> getType() {
        return Number.class;
    }

    @Override
    public Number parse(InputReader reader, Argument arg, CommandContext ctx) throws CommandParseException {
        if (arg.getType() == Integer.class) {
            return reader.readInt();
        }
        return reader.readDouble();
    }

    @Override
    public void suggest(Argument arg, CommandContext ctx, Suggestions suggestions) {

    }
}
