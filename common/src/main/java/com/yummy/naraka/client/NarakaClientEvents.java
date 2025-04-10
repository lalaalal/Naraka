package com.yummy.naraka.client;

import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.ComponentStyles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class NarakaClientEvents {
    public static void initialize() {
        ClientEvents.TICK_PRE.register(NarakaClientEvents::onClientTick);
        ClientEvents.CLIENT_STOPPING.register(NarakaClientEvents::onClientStopping);
    }

    private static void onClientTick(Minecraft minecraft) {
        ComponentStyles.tick();
    }

    private static void onClientStopping(Minecraft minecraft) {
        NarakaConfig.stop();
    }
}
