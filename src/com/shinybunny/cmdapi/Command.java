package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.annotations.Settings;
import com.shinybunny.cmdapi.arguments.Argument;
import com.shinybunny.cmdapi.arguments.SubCommandAdapter;
import com.shinybunny.cmdapi.exceptions.CommandExecutionException;
import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;
import com.shinybunny.cmdapi.utils.CommandResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a single command. A command is created for every valid method of an object's class passed in {@link CommandAPI#register(Object)}.
 * It can be created as a tree command as well if the object is annotated with <code>@</code>{@link Settings}.
 * A tree command will have only a single argument for the sub command parameter
 */
public class Command {

    private String name;
    private List<Argument> arguments;
    private Method method;
    private Object holder;
    private List<String> aliases;

    public Command(String name, Method method, Object holder) throws IncompatibleAnnotationException, NoAdapterFoundException {
        this.name = name;
        this.method = method;
        this.holder = holder;
        aliases = new ArrayList<>();
        createArguments(method);
        applySettings(method.getAnnotation(Settings.class));
    }

    public Command(Object holder, Settings settings) throws IncompatibleAnnotationException, NoAdapterFoundException {
        this.holder = holder;
        this.name = nameTree(holder.getClass().getSimpleName());
        this.arguments = new ArrayList<>();
        Argument scArg = new Argument("sub command", SubCommand.class, new Annotation[0],new SubCommandAdapter(CommandAPI.createSubCommands(holder)));
        arguments.add(scArg);
        applySettings(settings);
    }

    /**
     * Chooses a name for a tree command class
     * @param simpleName The class's simple name
     * @return A nicer name for it
     */
    private String nameTree(String simpleName) {
        if (simpleName.toLowerCase().startsWith("command")) {
            return simpleName.substring("command".length()).toLowerCase();
        } else {
            return simpleName.toLowerCase().endsWith("command") ? simpleName.substring(0, simpleName.length() - "command".length()) : simpleName.toLowerCase();
        }
    }

    /**
     * Applies the given settings annotation to this command.
     */
    public void applySettings(Settings settings) {
        if (settings == null) return;
        if (!settings.name().isEmpty())
            name = settings.name();
        aliases = Arrays.asList(settings.aliases());
    }

    private void createArguments(Method method) throws IncompatibleAnnotationException, NoAdapterFoundException {
        arguments = new ArrayList<>();
        for (Parameter p : method.getParameters()) {
            Class<?> type = CommandAPI.wrapPrimitive(p.getType());
            Argument arg = new Argument(p.getName(),type,p.getAnnotations(),CommandAPI.getArgumentAdapter(type));
            arguments.add(arg);
        }
    }

    /**
     * @return The name of the command. Same as the name of the method the command is made from.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The argument list parsed from the method parameters.
     */
    public List<Argument> getArguments() {
        return arguments;
    }

    /**
     * Executes the command and passing the {@link ParseResults} to the method's parameters.
     * @param results The {@link ParseResults} to use as parameters
     * @return A {@link CommandResult} object created by passing the returned object from invoking the method to {@link CommandResult#from(Object)}
     */
    public CommandResult run(ParseResults results) throws CommandExecutionException {
        List<Object> args = new ArrayList<>();
        for (Argument arg : arguments) {
            args.add(results.get(arg));
        }
        /*int i = 0;
        for (Parameter p : method.getParameters()) {
            Object o = args.get(i);
            System.out.println("passing " + o + " (" +(o == null ? "" : o.getClass()) + ", should be " + arguments.get(i).getType() + ") for parameter " + p.getName() + " (" + p.getType() + ")");
            i++;
        }*/
        Object obj;
        try {
            obj = method.invoke(holder,args.toArray());
        } catch (InvocationTargetException e) {
            throw new CommandExecutionException(e.getCause());
        } catch (IllegalAccessException e) {
            throw new CommandExecutionException(e);
        }
        return CommandResult.from(obj);
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
}
