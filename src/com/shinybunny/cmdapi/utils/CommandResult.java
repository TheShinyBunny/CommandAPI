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
            return o instanceof Boolean ? success((Boolean)o ? 1 : 0) : success;
        }
    }

    int successCount();

    default boolean isSuccessful() {
        return this.successCount() > 0;
    }

    int queryResult();

    default String getMessage() {
        return "";
    }

    static CommandResult result(final int result) {
        return new CommandResult() {
            public int successCount() {
                return 1;
            }

            public int queryResult() {
                return result;
            }
        };
    }

    static CommandResult result(final int result, final String message) {
        return new CommandResult() {
            public int successCount() {
                return 1;
            }

            public int queryResult() {
                return result;
            }

            public String getMessage() {
                return message;
            }
        };
    }

    CommandResult success = new CommandResult() {
        public int successCount() {
            return 1;
        }

        public int queryResult() {
            return 1;
        }
    };

    CommandResult fail = new CommandResult() {
        public int successCount() {
            return 0;
        }

        public int queryResult() {
            return 0;
        }
    };

    static CommandResult fail(final String message) {
        return new CommandResult() {
            public int successCount() {
                return 0;
            }

            public int queryResult() {
                return 0;
            }

            public String getMessage() {
                return message;
            }
        };
    }

    static CommandResult success(final int count) {
        return new CommandResult() {
            public int successCount() {
                return count;
            }

            public int queryResult() {
                return 1;
            }
        };
    }

    static CommandResult success(final int count, final String message) {
        return new CommandResult() {
            public int successCount() {
                return count;
            }

            public int queryResult() {
                return 1;
            }

            public String getMessage() {
                return message;
            }
        };
    }

    static CommandResult success(final String message) {
        return new CommandResult() {
            public int successCount() {
                return 1;
            }

            public int queryResult() {
                return 1;
            }

            public String getMessage() {
                return message;
            }
        };
    }

}
