package com.yummy.naraka.neoforge.init;

import com.mojang.brigadier.CommandDispatcher;
import com.yummy.naraka.init.CommandRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.function.Consumer;

public final class NeoForgeCommandRegistry implements NarakaEventBus {
    @MethodProxy(CommandRegistry.class)
    public static void register(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
        NARAKA_BUS.addListener(RegisterCommandsEvent.class, event -> {
            consumer.accept(event.getDispatcher());
        });
    }
}
