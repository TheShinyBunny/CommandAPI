package com.shinybunny.cmdapi;

public interface Sender {

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

    void sendMessage(String msg);

    void sendMessage(String msg, Object... formatArgs);

    void fail(String msg);

    void success(String msg);

    default boolean hasPermission(String permission) {
        return true;
    }

}
