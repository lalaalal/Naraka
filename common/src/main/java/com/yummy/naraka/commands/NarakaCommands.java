package com.yummy.naraka.commands;

import com.yummy.naraka.init.CommandRegistry;

public class NarakaCommands {
    public static void initialize() {
        CommandRegistry.register(StigmaCommand::register);
    }
}
