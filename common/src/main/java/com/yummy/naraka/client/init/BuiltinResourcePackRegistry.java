package com.yummy.naraka.client.init;

import com.yummy.naraka.Platform;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class BuiltinResourcePackRegistry {
    public static void register(ResourceLocation resourcePack, Component displayName) {
        if (!Platform.getInstance().isDevelopmentEnvironment()) {
            MethodInvoker.of(BuiltinResourcePackRegistry.class, "register")
                    .withParameterTypes(ResourceLocation.class, Component.class)
                    .invoke(resourcePack, displayName);
        }
    }
}
