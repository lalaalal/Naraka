package com.yummy.naraka.fabric.init;

import com.mojang.brigadier.CommandDispatcher;
import com.yummy.naraka.init.CommandRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Consumer;

public final class FabricCommandRegistry {
    @MethodProxy(CommandRegistry.class)
    public static void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
        CommandRegistrationCallback.EVENT.register((dispatcher, context, commandSelection) -> {
            consumer.accept(dispatcher);
        });
    }
}
