package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.arguments.Argument;
import com.shinybunny.cmdapi.arguments.ArgumentAdapter;
import com.shinybunny.cmdapi.exceptions.CommandExecutionException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;
import com.shinybunny.cmdapi.utils.CommandExecutor;
import com.shinybunny.cmdapi.utils.CommandResult;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * A command created by {@link CommandAPI#createCommand(String)}, that uses a {@link CommandExecutor} to run the command.
 */
public class DynamicCommand extends CommandBase {
    private CommandExecutor executor;
    private Predicate<CommandContext> requirement;

    public DynamicCommand(String name, List<Argument> arguments, CommandExecutor executor) {
        super(name);
        this.arguments = arguments;
        this.executor = executor;
    }

    @Override
    public CommandResult run(CommandContext ctx) throws CommandExecutionException {
        Object result;
        try {
            result = executor.run(ctx);
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
        return CommandResult.from(result);
    }

    @Override
    public void validateUse(CommandContext ctx) throws NoPermissionException {
        if (requirement != null) {
            if (!requirement.test(ctx)) {
                throw new NoPermissionException("You are not allowed to use this command!");
            }
        }
    }

    /**
     * Builds a new Command out of {@link Argument} objects
     */
    public static class Builder {

        private String name;
        private List<Argument> arguments = new ArrayList<>();
        private List<String> aliases = new ArrayList<>();
        private Predicate<CommandContext> requirement;
        private CommandManager manager;

        public Builder(CommandManager manager, String name) {
            this.name = name;
        }

        public Builder(String name) {
            this(CommandAPI.DEFAULT,name);
        }

        public Builder requires(Predicate<CommandContext> test) {
            this.requirement = test;
            return this;
        }

        public Builder aliases(String... aliases) {
            this.aliases = Arrays.asList(aliases);
            return this;
        }

        public Builder argument(Argument arg) {
            arguments.add(arg);
            return this;
        }

        public Builder argument(String name, ArgumentAdapter<?> adapter) {
            try {
                return argument(new Argument(name,adapter.getType(),adapter));
            } catch (NoAdapterFoundException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder argument(String name, Class<?> type) {
            return argument(name, manager.getArgumentAdapter(type));
        }

        public DynamicCommand build(CommandExecutor executor) {
            DynamicCommand cmd = new DynamicCommand(name, arguments, executor);
            cmd.aliases = aliases;
            cmd.requirement = requirement;
            manager.addCommand(cmd);
            return cmd;
        }

    }
}
