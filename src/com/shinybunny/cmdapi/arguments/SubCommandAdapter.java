package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.SubCommand;
import com.shinybunny.cmdapi.Suggestions;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.utils.InputReader;

import java.util.List;

public class SubCommandAdapter implements ArgumentAdapter<SubCommand> {

    private final List<SubCommand> subCommands;

    public SubCommandAdapter(List<SubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public Class<SubCommand> getType() {
        return SubCommand.class;
    }

    @Override
    public SubCommand parse(InputReader reader, Argument arg, CommandContext ctx) throws CommandParseException {
        String alias = reader.readWord();
        for (SubCommand cmd : subCommands) {
            if (cmd.getAliases().contains(alias.toLowerCase()) || cmd.getName().equalsIgnoreCase(alias)) {
                return cmd;
            }
        }
        throw new CommandParseException("Unknown sub command '" + alias + "'");
    }

    @Override
    public void suggest(Argument arg, CommandContext ctx, Suggestions suggestions) {

    }
}
