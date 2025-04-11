package com.yummy.naraka.event;

import com.yummy.naraka.invoker.MethodInvoker;

public abstract class EventHandler {
    public static void prepare() {
        MethodInvoker.invoke(EventHandler.class, "prepare");
    }
}
