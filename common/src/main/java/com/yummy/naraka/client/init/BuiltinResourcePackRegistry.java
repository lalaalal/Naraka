package com.yummy.naraka.client.init;

import com.yummy.naraka.client.NarakaClientServices;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public abstract class BuiltinResourcePackRegistry {
    public static void register(Identifier resourcePack, Component displayName) {
        NarakaClientServices.BUILTIN_RESOURCE_PACK_REGISTRY.register(resourcePack, displayName);
    }

    public interface Registrar {
        void register(Identifier resourcePack, Component displayName);
    }
}
