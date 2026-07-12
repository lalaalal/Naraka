package com.yummy.naraka.fabric.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.BuiltinResourcePackRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class FabricBuiltinResourcePackRegistry {
    @MethodProxy(BuiltinResourcePackRegistry.class)
    public static void register(ResourceLocation resourcePack, Component displayName) {
        FabricLoader.getInstance().getModContainer(NarakaMod.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    resourcePack,
                    modContainer,
                    displayName,
                    ResourcePackActivationType.NORMAL
            );
        });
    }
}
