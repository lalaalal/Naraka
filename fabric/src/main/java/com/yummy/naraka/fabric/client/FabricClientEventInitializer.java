package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.event.ClientEventInitializer;
import com.yummy.naraka.client.event.ClientEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public final class FabricClientEventInitializer implements ClientEventInitializer {
    @Override
    public void initialize() {
        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> ClientEvents.TICK_PRE.invoker().run(minecraft));
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> ClientEvents.TICK_POST.invoker().run(minecraft));
    }
}
