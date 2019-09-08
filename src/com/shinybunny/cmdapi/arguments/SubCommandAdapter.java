package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandAPI;
import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.MethodCommand;
import com.shinybunny.cmdapi.annotations.DefaultSubCommand;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;
import com.shinybunny.cmdapi.utils.InputReader;

import java.lang.reflect.Constructor;
import java.util.List;

public class SubCommandAdapter implements ArgumentAdapter<MethodCommand> {

    private final List<MethodCommand> subCommands;
    private MethodCommand defaultCommand;

    public SubCommandAdapter(Object holder) throws IncompatibleAnnotationException, NoAdapterFoundException {
        this.subCommands = CommandAPI.createSubCommands(holder);
        for (Constructor c : holder.getClass().getConstructors()) {
            if (c.isAnnotationPresent(DefaultSubCommand.class)) {
                defaultCommand = new MethodCommand("_default", c, holder);
            }
        }
    }

    @Override
    public Class<MethodCommand> getType() {
        return MethodCommand.class;
    }

    @Override
    public MethodCommand parse(InputReader reader, Argument arg, CommandContext ctx) throws CommandParseException {
        int pos = reader.getCursor();
        String alias = reader.readWord();
        for (MethodCommand cmd : subCommands) {
            if (cmd.getAliases().contains(alias.toLowerCase()) || cmd.getName().equalsIgnoreCase(alias)) {
                return cmd;
            }
        }
        if (defaultCommand != null) {
            reader.setCursor(pos);
            return defaultCommand;
        }
        throw new CommandParseException("Unknown sub command '" + alias + "'");
    }
}
