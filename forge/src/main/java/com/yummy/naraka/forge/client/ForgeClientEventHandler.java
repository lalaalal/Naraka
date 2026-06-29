package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.event.ClientEventHandler;
import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ForgeClientEventHandler implements NarakaEventBus {
    @MethodProxy(ClientEventHandler.class)
    public static void prepare() {
        FORGE_BUS.addListener(EventPriority.NORMAL, false, TickEvent.ClientTickEvent.class, event -> {
            if (event.phase == TickEvent.Phase.START)
                ClientEvents.TICK_PRE.invoker().run(Minecraft.getInstance());
        });
        FORGE_BUS.addListener(EventPriority.NORMAL, false, TickEvent.ClientTickEvent.class, event -> {
            if (event.phase == TickEvent.Phase.END)
                ClientEvents.TICK_POST.invoker().run(Minecraft.getInstance());
        });
    }
}
