package com.yummy.naraka.fabric.init;

import com.mojang.brigadier.CommandDispatcher;
import com.yummy.naraka.init.CommandRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class FabricCommandRegistry implements CommandRegistry.Registrar {
    @Override
    public void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
        CommandRegistrationCallback.EVENT.register((dispatcher, context, commandSelection) -> {
            consumer.accept(dispatcher);
        });
    }

    @Override
    public void register(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> consumer) {
        CommandRegistrationCallback.EVENT.register((dispatcher, context, commandSelection) -> {
            consumer.accept(dispatcher, context);
        });
    }
}
