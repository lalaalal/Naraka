package com.yummy.naraka.client;

import com.yummy.naraka.util.ComponentStyles;
import dev.architectury.event.events.client.ClientTickEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class NarakaClientEvents {
    public static void initialize() {
        ClientTickEvent.CLIENT_PRE.register(NarakaClientEvents::onClientTick);
    }

    private static void onClientTick(Minecraft minecraft) {
        ComponentStyles.tick();
    }
}
