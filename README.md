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
The annotations are a way to add custom validation and behaviour to the registeration process as well as parsing.
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

`parse(Sender sender,String input)` returns a `ParseResults`, containing a mapping for all parsed data from the input - the command used and the arguments.

`ParseResults.execute()` invokes the method associated with the command with all arguments, and returns a `CommandResult` object.

### The CommandResult
The `CommandResult` is an interface of the general result received from the command method, resolved from the object returned from the method.
The command result is made of a number and sometimes a message.
If the number is more than 0, it is successful.

A result of a command from a method return type could be:
`true/false`, so the Command Result will be 1 or 0.
`int` so the Command Result will become that number.
`String` will make the result successful and have that message.
`CommandResult` will just stay the same.
`Anything else` will be successful and set to 1.

`integer`, so the Command Result will be 
