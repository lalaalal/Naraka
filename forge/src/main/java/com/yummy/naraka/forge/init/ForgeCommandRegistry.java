package com.yummy.naraka.forge.init;

import com.mojang.brigadier.CommandDispatcher;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.init.CommandRegistry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ForgeCommandRegistry implements CommandRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
        FORGE_BUS.addListener(EventPriority.NORMAL, false, RegisterCommandsEvent.class, event -> {
            consumer.accept(event.getDispatcher());
        });
    }

    @Override
    public void register(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> consumer) {
        FORGE_BUS.addListener(EventPriority.NORMAL, false, RegisterCommandsEvent.class, event -> {
            consumer.accept(event.getDispatcher(), event.getBuildContext());
        });
    }
}
