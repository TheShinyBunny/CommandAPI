package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.Suggestions;
import com.shinybunny.cmdapi.annotations.MultiWord;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.utils.InputReader;

public class StringAdapter implements ArgumentAdapter<String> {
    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String parse(InputReader reader, Argument arg, CommandContext ctx) throws CommandParseException {
        return arg.hasAnnotation(MultiWord.class) ? reader.rest() : reader.readWord();
    }

    @Override
    public void suggest(Argument arg, CommandContext ctx, Suggestions suggestions) {

    }
}
