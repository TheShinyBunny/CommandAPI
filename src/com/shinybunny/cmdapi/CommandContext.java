package com.shinybunny.cmdapi;

public class CommandContext {
    private final String alias;
    private Command cmd;
    private final Sender sender;
    private final String input;

    public CommandContext(String alias, Command cmd, Sender sender, String input) {
        this.alias = alias;
        this.cmd = cmd;
        this.sender = sender;
        this.input = input;
    }

    public Command getCommand() {
        return cmd;
    }

    public Sender getSender() {
        return sender;
    }

    public String getAlias() {
        return alias;
    }

    public String getInput() {
        return input;
    }

    public void setCommand(Command cmd) {
        this.cmd = cmd;
    }
}
