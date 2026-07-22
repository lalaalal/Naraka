package com.yummy.naraka.client.init;

import com.yummy.naraka.client.service.NarakaClientServices;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class BuiltinResourcePackRegistry {
    public static void register(ResourceLocation resourcePack, Component displayName) {
        NarakaClientServices.BUILTIN_RESOURCE_PACK_REGISTRY.register(resourcePack, displayName);
    }

    public interface Registrar {
        void register(ResourceLocation resourcePack, Component displayName);
    }
}
