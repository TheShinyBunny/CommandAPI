package com.shinybunny.cmdapi.arguments;

import com.shinybunny.cmdapi.CommandAPI;
import com.shinybunny.cmdapi.CommandContext;
import com.shinybunny.cmdapi.CommandManager;
import com.shinybunny.cmdapi.annotations.Arg;
import com.shinybunny.cmdapi.exceptions.CommandParseException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;
import com.shinybunny.cmdapi.utils.InputReader;
import com.shinybunny.cmdapi.exceptions.InvalidArgumentException;

/**
 * Represents an argument of a command. An argument is created for each parameter of a method.<br/>
 * An argument is created as the sub command argument in tree commands.
 */
public class Argument {

    protected CommandManager manager;
    private String name;
    private Class<?> type;
    private String description;
    private ArgumentAdapter<?> adapter;
    protected boolean required = true;
    private Object defaultValue;
    protected boolean syntax = true;


    public Argument(CommandManager manager, String name, Class<?> type, ArgumentAdapter<?> argumentAdapter) throws NoAdapterFoundException {
        this.manager = manager;
        this.name = name;
        if (argumentAdapter == null) {
            throw new NoAdapterFoundException("No argument adapter found for type " + type,type);
        }
        this.adapter = argumentAdapter;
        this.type = type;
        this.defaultValue = adapter.getDefault();

    }

    public Argument(String name, Class<?> type) throws NoAdapterFoundException {
        this(CommandAPI.DEFAULT,name,type,CommandAPI.DEFAULT.getArgumentAdapter(type));
    }

    /**
     * The argument name.
     * If the compiler doesn't save parameter names, this will probably be arg0, arg1, etc. by default.
     * Use the {@link Arg} annotation to use a hardcoded name.
     */
    public String getName() {
        return name;
    }

    /**
     * The type of data the argument accepts - the type of its respective parameter.
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Parses this argument using its {@link ArgumentAdapter}.
     * @param reader The input reader to pass to the {@link ArgumentAdapter#parse(InputReader, Argument, CommandContext)} method
     * @param ctx The parsing context
     * @return The value to pass to the method
     * @throws CommandParseException If any parsing errors occurred.
     */
    public Object parse(InputReader reader, CommandContext ctx) throws CommandParseException {
        return adapter.parse(reader,this,ctx);
    }

    public Object getOutOfSyntax(CommandContext ctx) {
        return adapter.outOfSyntax(ctx);
    }

    public Object process(Object value, CommandContext ctx) throws InvalidArgumentException {
        return value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Whether this argument accepts <code>null</code> values, as defined by {@link ArgumentAdapter#nullable()}.
     */
    public boolean isNullable() {
        return adapter.nullable();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public ArgumentAdapter<?> getAdapter() {
        return adapter;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Argument && ((Argument) obj).name.equals(name) && ((Argument) obj).type == type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " (" + type.getSimpleName() + ")";
    }

    /**
     * Whether this argument is a part of the command's syntax, and should be parsed from the input.<br/>
     * For example, a {@link com.shinybunny.cmdapi.Sender} argument is not part of the syntax, as it's the source of the command.
     */
    public boolean isSyntax() {
        return syntax;
    }

}
