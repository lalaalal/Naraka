package com.yummy.naraka.init;

import com.mojang.brigadier.CommandDispatcher;
import com.yummy.naraka.service.NarakaServices;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class CommandRegistry {
    public static void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
        NarakaServices.COMMAND_REGISTRY.register(consumer);
    }

    public static void register(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> consumer) {
        NarakaServices.COMMAND_REGISTRY.register(consumer);
    }

    public interface Registrar {
        void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer);

        void register(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> consumer);
    }
}
