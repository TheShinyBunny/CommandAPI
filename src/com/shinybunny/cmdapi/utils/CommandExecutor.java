package com.shinybunny.cmdapi.utils;

import com.shinybunny.cmdapi.CommandContext;

@FunctionalInterface
public interface CommandExecutor {

    Object run(CommandContext ctx);

}
