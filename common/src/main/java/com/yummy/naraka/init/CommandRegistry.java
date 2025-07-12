package com.yummy.naraka.init;

import com.mojang.brigadier.CommandDispatcher;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class CommandRegistry {
    public static void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
        MethodInvoker.of(CommandRegistry.class, "register")
                .withParameterTypes(Consumer.class)
                .invoke(consumer);
    }

    public static void register(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> consumer) {
        MethodInvoker.of(CommandRegistry.class, "register")
                .withParameterTypes(BiConsumer.class)
                .invoke(consumer);
    }
}
