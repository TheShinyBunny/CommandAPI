package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.annotations.Command;

/**
 * The <code>Sender</code> is an object representing the user that executed a command.
 * This could be a specific user in a server, or a "fake" user, like the console or the program itself.
 * The Sender interface has a few methods for sending a message back to the user.
 */
public interface Sender {

    /**
     * The CONSOLE sender, used to send command feedback to the {@link System#out console output stream}.
     */
    Sender CONSOLE = new Sender() {
        public void sendMessage(String message) {
            System.out.println(message);
        }

        public void sendMessage(String message, Object... format) {
            System.out.printf(message, format);
        }

        public void fail(String message) {
            System.out.println("error: " + message);
        }

        public void success(String message) {
            System.out.println("success: " + message);
        }
    };

    /**
     * Sends a normal message to the sender, about information from the command execution
     * @param msg The message to send
     */
    void sendMessage(String msg);

    /**
     * Sends a message to the user with string format args.<br/>
     * Typically the implementation should be <code>sendMessage(String.format(msg,formatArgs));</code>
     * @param msg The message
     * @param formatArgs The format args to use with {@link String#format(String, Object...) String.format()}
     */
    void sendMessage(String msg, Object... formatArgs);

    /**
     * Sends an error message to the user, about any error in the command execution. Could be parsing errors, or errors from the command implementation.
     * Typically should highlight the feedback text in red, if the framework supports that.
     * @param msg
     */
    void fail(String msg);

    /**
     * Sends a success message to the user, about a success of a command execution.
     * Typically should highlight the feedback text in green, if the framework supports that.
     * @param msg
     */
    void success(String msg);

    /**
     * Checks for the user's permission. Used by {@link Command#permission()}.
     * @param permission The permission ID to check
     * @return true if the user has that permission
     */
    default boolean hasPermission(String permission) {
        return true;
    }

}
