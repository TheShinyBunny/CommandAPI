package com.shinybunny.cmdapi.utils;

import com.shinybunny.cmdapi.CommandContext;

@FunctionalInterface
public interface VoidCommandExecutor extends CommandExecutor {

    void runVoid(CommandContext ctx);

    @Override
    default Void run(CommandContext ctx) {
        runVoid(ctx);
        return null;
    }
}
