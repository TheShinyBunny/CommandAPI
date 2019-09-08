package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.annotations.Command;
import com.shinybunny.cmdapi.arguments.Argument;
import com.shinybunny.cmdapi.arguments.ParameterArgument;
import com.shinybunny.cmdapi.arguments.SubCommandAdapter;
import com.shinybunny.cmdapi.exceptions.CommandExecutionException;
import com.shinybunny.cmdapi.exceptions.IncompatibleAnnotationException;
import com.shinybunny.cmdapi.exceptions.NoAdapterFoundException;
import com.shinybunny.cmdapi.utils.CommandResult;

import javax.naming.NoPermissionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a single registered command using the method parser. A command is created for every valid method of an object's class passed in {@link CommandAPI#register(Object)}.
 * It can be created as a tree command as well if the object class is annotated with <code>@</code>{@link com.shinybunny.cmdapi.annotations.Command}.
 * A tree command will have only a single argument for the sub command
 */
public class MethodCommand extends CommandBase {

    private Executable method;
    private Object holder;
    private Command settings;

    public MethodCommand(CommandManager manager, String name, Executable method, Object holder) throws IncompatibleAnnotationException, NoAdapterFoundException {
        super(manager,name);
        this.name = name;
        this.method = method;
        this.holder = holder;
        aliases = new ArrayList<>();
        createArguments(method);
        applySettings(method.getAnnotation(Command.class));
    }

    public MethodCommand(CommandManager manager, Object holder, Command settings) throws IncompatibleAnnotationException, NoAdapterFoundException {
        super(manager,nameTree(holder.getClass().getSimpleName()));
        this.holder = holder;
        Argument scArg = new ParameterArgument(manager,"sub command", new Annotation[0],MethodCommand.class, new SubCommandAdapter(holder));
        arguments.add(scArg);
        applySettings(settings);
    }

    /**
     * Chooses a name for a tree command class
     * @param simpleName The class's simple name
     * @return A nicer name for it
     */
    private static String nameTree(String simpleName) {
        if (simpleName.toLowerCase().startsWith("command")) {
            return simpleName.substring("command".length()).toLowerCase();
        } else {
            return simpleName.toLowerCase().endsWith("command") ? simpleName.substring(0, simpleName.length() - "command".length()) : simpleName.toLowerCase();
        }
    }

    /**
     * Applies the given settings annotation to this command.
     */
    public void applySettings(Command settings) {
        if (settings == null) return;
        this.settings = settings;
        if (!settings.name().isEmpty())
            name = settings.name();
        aliases = Arrays.asList(settings.aliases());
    }

    private void createArguments(Executable method) throws IncompatibleAnnotationException, NoAdapterFoundException {
        for (Parameter p : method.getParameters()) {
            Class<?> type = CommandAPI.wrapPrimitive(p.getType());
            Argument arg = new ParameterArgument(manager,p.getName(),p.getAnnotations(),type,manager.getArgumentAdapter(type));
            arguments.add(arg);
        }
    }

    /**
     * Executes the command and passing the {@link ParseResults} to the method's parameters.
     * @param ctx The {@link CommandContext} to use as parameters
     * @return A {@link CommandResult} object created by passing the returned object from invoking the method to {@link CommandResult#from(Object)}
     */
    @Override
    public CommandResult run(CommandContext ctx) throws CommandExecutionException {
        List<Object> args = new ArrayList<>();
        for (Argument arg : arguments) {
            args.add(ctx.get(arg));
        }
        /*int i = 0;
        for (Parameter p : method.getParameters()) {
            Object o = args.get(i);
            System.out.println("passing " + o + " (" +(o == null ? "" : o.getClass()) + ", should be " + arguments.get(i).getType() + ") for parameter " + p.getName() + " (" + p.getType() + ")");
            i++;
        }*/
        Object obj;
        try {
            obj = method instanceof Method ? ((Method) method).invoke(holder,args.toArray()) : ((Constructor)method).newInstance(args.toArray());
        } catch (InvocationTargetException e) {
            throw new CommandExecutionException(e.getCause());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new CommandExecutionException(e);
        }
        return CommandResult.from(obj);
    }

    public Command getSettings() {
        return settings;
    }

    public String getNoPermissionMessage() {
        return settings == null ? super.getNoPermissionMessage() : settings.noPermissionMessage();
    }

    public void validateUse(CommandContext ctx) throws NoPermissionException {
        if (settings != null) {
            if (!settings.permission().isEmpty()) {
                if (!ctx.hasPermission(settings.permission())) {
                    throw new NoPermissionException(settings.noPermissionMessage());
                }
            }
        }
    }
}
