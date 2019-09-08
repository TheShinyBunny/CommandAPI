package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.annotations.Command;
import com.shinybunny.cmdapi.arguments.Argument;
import com.shinybunny.cmdapi.exceptions.CommandExecutionException;
import com.shinybunny.cmdapi.utils.CommandResult;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase {

    protected final CommandManager manager;
    protected String name;
    protected List<Argument> arguments;
    protected List<String> aliases;

    public CommandBase(CommandManager manager, String name) {
        this.manager = manager;
        this.name = name;
        arguments = new ArrayList<>();
        aliases = new ArrayList<>();
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public abstract CommandResult run(CommandContext ctx) throws CommandExecutionException;

    public String getNoPermissionMessage() {
        return Command.DEFAULT_NO_PERMS_MESSAGE;
    }

    public abstract void validateUse(CommandContext ctx) throws NoPermissionException;

}
