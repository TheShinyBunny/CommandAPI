package com.shinybunny.cmdapi;

import com.shinybunny.cmdapi.arguments.Argument;

public class CommandContext implements Sender {
    private final String alias;
    private CommandBase cmd;
    private final Sender sender;
    private final String input;
    private ParseResults parseResults;

    public CommandContext(String alias, CommandBase cmd, Sender sender, String input) {
        this.alias = alias;
        this.cmd = cmd;
        this.sender = sender;
        this.input = input;
        this.parseResults = new ParseResults(this);
    }

    public CommandBase getCommand() {
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

    public void setCommand(MethodCommand cmd) {
        this.cmd = cmd;
    }

    public <T> T get(String name) {
        return (T) parseResults.get(name);
    }

    public <T> T get(String name, Class<T> type) {
        return type.cast(parseResults.get(name));
    }

    public <T> T get(Argument arg) {
        return (T) parseResults.get(arg);
    }

    public <T> T get(Argument arg, Class<T> type) {
        return type.cast(parseResults.get(arg));
    }

    @Override
    public void sendMessage(String msg) {
        sender.sendMessage(msg);
    }

    @Override
    public void sendMessage(String msg, Object... formatArgs) {
        sender.sendMessage(msg,formatArgs);
    }

    @Override
    public void fail(String msg) {
        sender.fail(msg);
    }

    @Override
    public void success(String msg) {
        sender.success(msg);
    }

    public void addArgument(ParseResults.Entry entry) {
        parseResults.addEntry(entry);
    }

    public ParseResults getResults() {
        return parseResults;
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
}
