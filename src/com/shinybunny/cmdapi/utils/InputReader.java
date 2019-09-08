package com.shinybunny.cmdapi.utils;

import com.shinybunny.cmdapi.exceptions.CommandParseException;

import java.util.function.Predicate;

public class InputReader {

    private String string;
    private int cursor = 0;

    public InputReader(String input) {
        this.string = input;
    }

    public String getString() {
        return string;
    }

    public char read() {
        return string.charAt(cursor++);
    }

    public int getCursor() {
        return cursor;
    }

    public void skip() {
        cursor++;
    }

    public char peek() {
        return string.charAt(cursor);
    }

    public void skipSpace() {
        skipAll(' ');
    }

    public boolean canRead() {
        return cursor + 1 <= string.length();
    }

    public void skipAll(char c) {
        while (canRead() && peek() == c)
            skip();
    }

    public void skipAll(Predicate<Character> toSkip) {
        while (canRead() && toSkip.test(peek()))
            skip();
    }

    public String readWhile(Predicate<Character> predicate) {
        int pos = cursor;
        skipAll(predicate);
        return select(pos,cursor);
    }

    public String select(int start, int end) {
        return string.substring(start,end);
    }

    public int readInt() throws CommandParseException {
        String s = readWhile(this::isAllowedInNumber);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new CommandParseException("Invalid number " + s);
        }
    }

    public double readDouble() throws CommandParseException {
        String s = readWhile(this::isAllowedInNumber);
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new CommandParseException("Invalid decimal number " + s);
        }
    }

    public String readWord() {
        return readWhile(this::isAllowedInWord);
    }

    public boolean readBoolean() throws CommandParseException {
        String s = readWord();
        if ("true".equalsIgnoreCase(s)) {
            return true;
        } else if ("false".equalsIgnoreCase(s)) {
            return false;
        }
        throw new CommandParseException("Invalid boolean " + s);
    }

    private boolean isAllowedInNumber(char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    private boolean isAllowedInWord(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.' || c == '+';
    }

    public String rest() {
        return select(cursor,string.length());
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }
}
