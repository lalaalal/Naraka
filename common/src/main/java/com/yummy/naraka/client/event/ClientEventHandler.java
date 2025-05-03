package com.yummy.naraka.client.event;

import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class ClientEventHandler {
    public static void prepare() {
        MethodInvoker.invoke(ClientEventHandler.class, "prepare");
    }
}
