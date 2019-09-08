package com.shinybunny.cmdapi.utils;

public interface CommandResult {

    static CommandResult from(Object o) {
        if (o instanceof CommandResult) {
            return (CommandResult)o;
        } else if (o instanceof Number) {
            return result(((Number) o).intValue());
        } else if (o instanceof String) {
            return success((String)o);
        } else {
            return o instanceof Boolean ? isTrue((Boolean)o) : success;
        }
    }


    boolean success();

    int result();

    default String getMessage() {
        return "";
    }

    static CommandResult result(final int result) {
        return new CommandResult() {
            public boolean success() {
                return true;
            }

            public int result() {
                return result;
            }
        };
    }

    static CommandResult result(final int result, final String message) {
        return new CommandResult() {
            public boolean success() {
                return true;
            }

            public int result() {
                return result;
            }

            public String getMessage() {
                return message;
            }
        };
    }

    CommandResult success = new CommandResult() {
        public boolean success() {
            return true;
        }

        public int result() {
            return 1;
        }
    };

    CommandResult fail = new CommandResult() {
        public boolean success() {
            return false;
        }

        public int result() {
            return 0;
        }
    };

    static CommandResult fail(final String message) {
        return new CommandResult() {
            public boolean success() {
                return false;
            }

            public int result() {
                return 0;
            }

            public String getMessage() {
                return message;
            }
        };
    }

    static CommandResult success(final String message) {
        return new CommandResult() {
            public boolean success() {
                return true;
            }

            public int result() {
                return 1;
            }

            public String getMessage() {
                return message;
            }
        };
    }

    static CommandResult isTrue(boolean b) {
        return new CommandResult() {
            @Override
            public boolean success() {
                return b;
            }

            @Override
            public int result() {
                return 1;
            }
        };
    }

}
