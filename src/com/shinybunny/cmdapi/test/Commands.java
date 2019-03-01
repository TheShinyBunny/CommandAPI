package com.shinybunny.cmdapi.test;

import com.shinybunny.cmdapi.Sender;
import com.shinybunny.cmdapi.annotations.Arg;
import com.shinybunny.cmdapi.annotations.Default;
import com.shinybunny.cmdapi.annotations.Range;
import com.shinybunny.cmdapi.annotations.Settings;
import com.shinybunny.cmdapi.utils.CommandResult;

import java.util.Random;

public class Commands {

    public CommandResult dice(Sender sender,
                              @Arg("min") @Default(number = 1) int min,
                              @Arg("max") @Default(number = 6) int max,
                              @Arg("rolls") @Default(number = 1) int rolls) {
        if (min > max) {
            sender.fail("Minimum number must be smaller than the maximum number!");
            return CommandResult.fail;
        } else {
            Random r = new Random();
            int c = 0;
            for(int i = 0; i < rolls; ++i) {
                int x = r.nextInt(max - min + 1) + min;
                sender.sendMessage("Rolled a " + x + "!");
                c += x;
            }
            return CommandResult.result(c);
        }
    }

    @Settings(name = "coins")
    public static class CoinsCommand {

        public void add(Sender sender,
                        @Arg("amount") @Range(min = 1) int amount) {
            sender.sendMessage("Added " + amount + " coins!");
        }

        public void take(Sender sender,
                         @Arg("amount") int amount) {
            sender.sendMessage("Taken away " + amount + " coins...");
        }

    }

}
