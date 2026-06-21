package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.event.ClientEventInitializer;
import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class NeoForgeClientEventInitializer implements ClientEventInitializer, NarakaEventBus {
    @Override
    public void initialize() {
        NEOFORGE_BUS.addListener(ClientTickEvent.Pre.class, _ -> ClientEvents.TICK_PRE.invoker().run(Minecraft.getInstance()));
        NEOFORGE_BUS.addListener(ClientTickEvent.Post.class, _ -> ClientEvents.TICK_POST.invoker().run(Minecraft.getInstance()));
    }
}
