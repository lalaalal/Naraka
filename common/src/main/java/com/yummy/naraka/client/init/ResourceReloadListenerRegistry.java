package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Supplier;

public abstract class ResourceReloadListenerRegistry {
    public static void register(Identifier location, Supplier<PreparableReloadListener> listener) {
        MethodInvoker.of(ResourceReloadListenerRegistry.class, "register")
                .withParameterTypes(Identifier.class, Supplier.class)
                .invoke(location, listener);
    }
}
