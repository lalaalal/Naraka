package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class ResourceReloadListenerRegistry {
    public static void register(ResourceLocation location, Supplier<PreparableReloadListener> listener) {
        MethodInvoker.of(ResourceReloadListenerRegistry.class, "register")
                .withParameterTypes(ResourceLocation.class, Supplier.class)
                .invoke(location, listener);
    }
}
