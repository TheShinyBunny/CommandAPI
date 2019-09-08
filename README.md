# CommandAPI
A simple yet awesome way to create a command system!
By using some advanced method parsing technology and some dark magic, this API allows you to just write your commands as methods and implement them inside! It's that simple!

## How do the commands look like?

Every command for this API is simply a method, where its name will become the command name, its parameters will become the command arguments, and whatever happens when executing that command is implemented inside the method. Every command syntax consists of the method name, following by the arguments separated by spaces (single arguments with spaces are possible).
 
Here are a few examples:

A command that prints the given number:
```java
public void printnum(int num) {
  System.out.println("Your number is: " + num);
}
```
So, by providing an input of `"printnum 15"` it will print `Your number is: 15`.

A command that sends the sender a dice result:
```java
public void dice(Sender sender, 
                @Range(min=1) @Default(number=1) int min,
                @Range(min=2) @Default(number=6) int max) {
  Random r = new Random();
  int x = r.nextInt(max - min + 1) + min;
  sender.sendMessage("You rolled a " + x + "!");
}
```
Note that `Sender` is not part of the syntax, thus will not be parsed from the input. More on that later on.
The annotations are a way to add custom validation and behaviour to the registration process as well as parsing.
So in this case, `@Range` determines the minimum allowed value for the `min` or `max` arguments, and if provided a number smaller then the minimum or greater than the maximum, an InvalidArgumentException you can catch is thrown. It will have a message you can send to the user.

The `@Default` annotation makes the argument be optional, so if it is not provided it will use the value described in the annotation.

Running `dice` will send:
`You rolled a 3!`
(3 is a random number between 1-6)


Running `dice 4` will send:
`You rolled a 5!`
(5 is a random number between 4-6)

Running `dice 10 30` will send:
`You rolled a 23!`
(23 is a random number between 10-30)

## How do I use the API?
The first thing you might want to do is add the API as a dependency.. duh..

But then, it is recommended to make a Class dedicated for all of your command:
```java
public class MyCommands {
  // all command methods will go here
}
```

Then construct your commands methods inside. A command will NOT be registered if its `static`.
It won't be registered as well if you annotate the method with `@DontRegister`, so that way you can prevent instance methods from becoming a command.

All other methods in that class will be registered as commands.

To register those commands, just use one of:
```java
CommandAPI.register(new MyCommands());
```
or instead:
```java
CommandAPI.register(MyCommands.class);
```

(use these register methods at the start of your program, or any time you want to add them to the system)

#### Dynamic commands
Another way of creating commands is programmatically, using `CommandAPI.createCommand(String name)`.

### Executing commands
Let's say you received a command input string from the user and you want to execute it using the API. All you gotta do is:
```java
try {
  // replace Sender.CONSOLE with your own sender adapter (explained in detail later)
  // input is the command string sent from the user
  parse(Sender.CONSOLE,input).execute();
} catch (Exception e) { // catch all exceptions together or implement your own handling of different problems with the input
  e.printStackTrace();
}
```

`CommandAPI.parse(Sender sender, String input)` returns a `ParseResults`, containing a mapping for all parsed data from the input - the command used and the arguments.

`ParseResults.execute()` invokes the method associated with the command with all arguments, and returns a `CommandResult` object.

### The CommandResult
The `CommandResult` is an interface of the general result received from the command method, resolved from the object returned from the method.
The command result is made out of a success count, a result and a message.
If the success count is 1, the result should be 1 as well. If the result is greater than 0, the success count is 1.

A method return type could be:

`true/false`, so the Command Result will be 1 or 0.

`int` so the Command Result will become that number.

`String` will make the result successful and have that message.

`CommandResult` will just stay the same.

`Anything else (including void)` will set success count to 1.

A use example could be:
```java
public String multiply(double factor1, double factor2) {
  return "The product of multiplying " + factor1 + " * " + factor2 + " is " + (factor1 * factor2);
}

public static void executeCommand(String input) {
  try {
    CommandResult result = CommandAPI.parse(Sender.CONSOLE,input).execute();
    System.out.println(result.getMessage());
  } catch (Exception e) {
    System.out.println(e.getMessage());
  }
}
```

## Adapters
The CommandAPI provides easy to use adapters for custom processing of annotations and argument types.

### ArgumentAdapter
If you have a special object type you'd like the API to recognize and parse from the input, you can create an ArgumentAdapter for it.

So, let's say you have a User class in your project, and you want to use it as an argument:
```java
public void kick(User user, String reason) {
  user.kick(reason);
}
```
Here we are using a parameter type of `User`. If we don't register an ArgumentAdapter for it before registering the command class, the API will throw a `NoAdapterFoundException`.

So let's create an adapter for User:
```java
public class UserAdapter implements ArgumentAdapter<User> {
  
  @Override
  public Class<User> getType() {
    return User.class;
  }
  
  @Override
  public User parse(InputReader reader, Argument arg, CommandContext ctx) throws CommandParseException {
    return User.getUserByName(reader.readWord());
  }
}
```

In this example we get introduced to some new API components.

The `InputReader` is a nice and simple thing to help parse the input easily, and it will be passed through all arguments.
Imagine the InputReader as a string, and a cursor indicating where the caret is located in that string. Now as that string is the input, it will move the cursor through all arguments until it comes to the end of the input.

The `Argument` is the object indicating the argument in the command. You can get its name, type is it required, is it part of the syntax, etc.

The `CommandContext` contains all information about the command parsing and execution process - The command used, the alias used, the Sender and the original input.

`reader.readWord()` reads and returns the string until the first space. The cursor is moved to the end of that string.


To register an `ArgumentAdapter`, just use
```java
CommandAPI.registerArgumentAdapter(new MyAdapter());
```


### AnnotationAdapter

Annotation adapters are used to make annotations to parameters alter the way the argument is processed. They can change the value, validate the input, add properties to the argument for the command documentation, etc.

For example, the `@Default` annotation applies a default value to any primitive-type argument:
```java
// inside the @Default class
class Adapter implements AnnotationAdapter<Default> {

    @Override
    public Class<Default> getAnnotationType() {
        return Default.class;
    }

    @Override
    public Object process(Object value, Default annotation, ParameterArgument arg, CommandContext ctx) throws InvalidArgumentException {
        if (value == null) {
            return useDefault(annotation,arg.getType());
        }
        return value;
    }
}
```

In the above example, an `AnnotationAdapter<Default>` is implemented. We provide the annotation class in `getAnnotationType`, and implement the processing of an argument in **execution time**.

The `process(Object,Annotation,ParameterArgument,CommandContext)` method is called right after the value is parsed from the input, or, in case this wasn't part of the syntax, after an `outOfSyntax` value is resolved. 

(`ParameterArgument` is an argument that has annotations, representing a method parameter).

Annotation adapters can influence the registration process too. The `init(ParameterArgument,Annotation)` is called on an argument when the command is registered via `CommandAPI.register(Class/Object)`. It can be used to apply initial modifications to the argument, such as a different name, description, `required` state, incompatible annotations, etc.

## Custom and Multiple Command Systems

If you, for some reason, need more than one command system, with distinct command and adapters, or override our awesome default system, you can use the `CommandManager` class.

This class is the common handler of almost all components in the API. The `CommandAPI` class we demonstrated thus far, delegates to a default `CommandManager` instance, with all of the default implementation.

You can construct your own CommandManager for every command system you need, and you can extend this class and override any method you need (all methods are protected or public).

