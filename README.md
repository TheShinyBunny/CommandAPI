# CommandAPI
A simple yet awesome way to create a command system!
By using some advanced method parsing technology and some dark magic, this API allows you to just write your commands as methods and implement them inside! It's that simple!

## How do I create commands?
The first thing you might want to do is make a Class for all of your command:
```java
public class MyCommands {
  // all command methods will go here
}
```

Every command for this API is simply a method, where its name will become the command name, its parameters will become the command arguments, and whatever happens when executing that command is implemented inside the method. Every command syntax consists of the method name, following by the arguments separated by spaces (single arguments with spaces are possible).
Here are a few examples:

A command that prints the given number:
```java
public void sayHello(int num) {
  System.out.println("Your number is: " + num);
}
```
So, by providing an input of `"sayhello 15"` it will print `Your number is: 15`.

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
