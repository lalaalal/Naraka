package com.yummy.naraka.forge.init;

import com.mojang.brigadier.CommandDispatcher;
import com.yummy.naraka.init.CommandRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.function.Consumer;

public final class ForgeCommandRegistry implements NarakaEventBus {
    @MethodProxy(CommandRegistry.class)
    public static void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
        FORGE_BUS.addListener(EventPriority.NORMAL, false, RegisterCommandsEvent.class, event -> {
            consumer.accept(event.getDispatcher());
        });
    }
}
