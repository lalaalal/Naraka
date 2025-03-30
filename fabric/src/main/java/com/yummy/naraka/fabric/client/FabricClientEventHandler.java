package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.event.ClientEventHandler;
import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.proxy.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricClientEventHandler {
    @MethodProxy(ClientEventHandler.class)
    public static void prepare() {
        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> ClientEvents.TICK_PRE.invoker().run(minecraft));
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> ClientEvents.TICK_POST.invoker().run(minecraft));
    }
}
