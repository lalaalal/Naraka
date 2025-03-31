package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.event.ClientEventHandler;
import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.neoforge.NarakaEventBus;
import com.yummy.naraka.proxy.MethodProxy;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class NeoForgeClientEventHandler implements NarakaEventBus {
    @MethodProxy(ClientEventHandler.class)
    public static void prepare() {
        NEOFORGE_BUS.addListener(ClientTickEvent.Pre.class, event -> ClientEvents.TICK_PRE.invoker().run(Minecraft.getInstance()));
        NEOFORGE_BUS.addListener(ClientTickEvent.Post.class, event -> ClientEvents.TICK_POST.invoker().run(Minecraft.getInstance()));
    }
}
