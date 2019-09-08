package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.annotations.*;
import com.shinybunny.cmdapi.arguments.*;
import com.shinybunny.cmdapi.exceptions.*;

import javax.naming.NoPermissionException;
import java.util.*;

/**
 * <h1>CommandAPI</h1>
 * The <code>CommandAPI</code> is a useful API for creating commands (yeah I think you got that yourself).<br/>
 * This API uses a different way to create commands than you probably ever saw.
 * Each command is simply a method, where its name is the name of the method, its arguments are translated from the method's parameters, and the command execution implementation is done inside the method. Simple stuff!
 * <br/>
 * Here's an example of a command that is simple and valid that this system can use:
 * <pre>
 *     public void dice(Sender sender,
 *                      {@literal @}Arg("min") @Range(min = 1) @Default(number = 1) int min,
 *                      {@literal @}Arg("max") @Range(min = 2) @Default(number = 6) int max,
 *                      {@literal @}Arg("seed") @Default(number = 0) long seed) {
 *          // roll a dice
 *     }
 * </pre>
 * This class delegates to a default {@link CommandManager}, which implements all function in this API.
 * If you need to change the default mechanics or have multiple command systems, use the command manager instead. Otherwise, you can use this class statically.
 */
public class CommandAPI {

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<>();
    public static CommandManager DEFAULT = new CommandManager();

    /*private static void test() {
        registerSafe(new Commands());
        registerSafe(new Commands.CoinsCommand());
        String in;
        Scanner s = new Scanner(System.in);
        while (true) {
            in = s.nextLine();
            if ("exit".equalsIgnoreCase(in)) break;
            try {
                parse(Sender.CONSOLE,in).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    public static <T> Class<T> wrapPrimitive(Class<T> c) {
        return c.isPrimitive() ? (Class<T>) PRIMITIVES_TO_WRAPPERS.get(c) : c;
    }



    static {
        PRIMITIVES_TO_WRAPPERS.put(Boolean.TYPE, Boolean.class);
        PRIMITIVES_TO_WRAPPERS.put(Byte.TYPE, Byte.class);
        PRIMITIVES_TO_WRAPPERS.put(Character.TYPE, Character.class);
        PRIMITIVES_TO_WRAPPERS.put(Double.TYPE, Double.class);
        PRIMITIVES_TO_WRAPPERS.put(Float.TYPE, Float.class);
        PRIMITIVES_TO_WRAPPERS.put(Integer.TYPE, Integer.class);
        PRIMITIVES_TO_WRAPPERS.put(Long.TYPE, Long.class);
        PRIMITIVES_TO_WRAPPERS.put(Short.TYPE, Short.class);
        PRIMITIVES_TO_WRAPPERS.put(Void.TYPE, Void.class);

    }

    /**
     * Register an {@link AnnotationAdapter} to the api.
     */
    public void registerAnnotationAdapter(AnnotationAdapter<?> adapter) {
        DEFAULT.registerAnnotationAdapter(adapter);
    }

    /**
     * Register an {@link ArgumentAdapter} to the api.
     */
    public void registerArgumentAdapter(ArgumentAdapter<?> adapter) {
        DEFAULT.registerArgumentAdapter(adapter);
    }

    /**
     * Gets the command uses the specified alias. Note that if more than one command has that alias (which is NOT supported, be careful!), only the first iterated command will be returned.
     * @param alias The alias to search for, and try matching it with all command names or aliases.
     * @return The command having that alias, or <code>null</code> if none found.
     */
    public CommandBase getCommand(String alias) {
        return DEFAULT.getCommand(alias);
    }

    /**
     * Registers a new command holder. A command holder is a class containing all methods to parse as commands.<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;In case the provided object is annotated with {@link com.shinybunny.cmdapi.annotations.Command},
     * it will be treated as a tree command and all of its methods will be its sub commands.<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;In case it doesn't,
     * all methods will be individual commands.
     * <br/>
     * To exclude a method, you can do one of the following:
     * <ol>
     *     <li>Make the method <code>static</code>. Static methods will not be registered.</li>
     *     <li>Annotate the method with <code>@</code>{@link DontRegister}</li>
     * </ol>
     * @param holder The instance of a class to register.
     * @exception IncompatibleAnnotationException if an annotation of some argument is not compatible with its type. For example, {@link Range @Range} for a non-numerical value.
     * @exception NoAdapterFoundException for any annotation or argument of a type that has no matching registered adapter.
     */
    public static void register(Object holder) throws IncompatibleAnnotationException, NoAdapterFoundException {
        DEFAULT.register(holder);
    }

    /**
     * Registers a new instance of the provided class.
     * @param cls The class to instantiate, using {@link Class#newInstance()}
     */
    public static void register(Class<?> cls) {
        DEFAULT.register(cls);
    }

    public static void registerSafe(Object holder) {
        try {
            register(holder);
        } catch (IncompatibleAnnotationException | NoAdapterFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all commands registered in the CommandAPI
     * @return A list of all commands. This list is not a copy, so it's possible to modify it.
     */
    public List<CommandBase> getCommands() {
        return DEFAULT.getCommands();
    }

    /**
     * Create a command programmatically
     * @param name The command name
     * @return A new {@link DynamicCommand.Builder} to build the command.
     */
    public static DynamicCommand.Builder createCommand(String name) {
        return new DynamicCommand.Builder(name);
    }

    /**
     * Parses, processes and validates a command input.<br/>
     * The provided input should start with the command alias, following with its arguments separated by spaces.
     * @param sender The {@link Sender} who executed the command.
     * @param input The command string the sender runs.
     * @return A new {@link ParseResults} object containing a mapping of all parsed information.
     * @throws UnknownCommandException if the provided command alias is not found.
     * @throws InvalidArgumentException if an argument validation exception occurs.
     * @throws MissingArgumentException if a required argument is not provided.
     * @throws CommandParseException if an argument parsing exception occurs.
     */
    public ParseResults parse(Sender sender, String input) throws UnknownCommandException, InvalidArgumentException, MissingArgumentException, CommandParseException, NoPermissionException {
        return DEFAULT.parse(sender,input);
    }


}
