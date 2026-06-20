package com.yummy.naraka.client.event;

import com.yummy.naraka.invoker.MethodInvoker;

public abstract class ClientEventHandler {
    public static void prepare() {
        MethodInvoker.invoke(ClientEventHandler.class, "prepare");
    }
}
