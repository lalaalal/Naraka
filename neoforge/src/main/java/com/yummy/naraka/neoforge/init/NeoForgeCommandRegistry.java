package com.yummy.naraka.neoforge.init;

import com.mojang.brigadier.CommandDispatcher;
import com.yummy.naraka.init.CommandRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class NeoForgeCommandRegistry implements NarakaEventBus, CommandRegistry.Registrar {
    @Override
    public void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
        NEOFORGE_BUS.addListener(RegisterCommandsEvent.class, event -> {
            consumer.accept(event.getDispatcher());
        });
    }

    @Override
    public void register(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> consumer) {
        NEOFORGE_BUS.addListener(RegisterCommandsEvent.class, event -> {
            consumer.accept(event.getDispatcher(), event.getBuildContext());
        });
    }
}
