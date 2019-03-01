package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.Sender;
import com.shinybunny.cmdapi.Suggestions;
import com.shinybunny.cmdapi.utils.InputReader;

public class SenderAdapter implements ArgumentAdapter<Sender> {
    @Override
    public Class<Sender> getType() {
        return Sender.class;
    }

    @Override
    public Sender parse(InputReader reader, Argument arg, CommandContext ctx) {
        return null;
    }

    @Override
    public Object outOfSyntax(CommandContext ctx) {
        return ctx.getSender();
    }

    @Override
    public void suggest(Argument arg, CommandContext ctx, Suggestions suggestions) {

    }
}
