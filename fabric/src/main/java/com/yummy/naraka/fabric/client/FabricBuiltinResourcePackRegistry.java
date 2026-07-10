package com.yummy.naraka.fabric.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.BuiltinResourcePackRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class FabricBuiltinResourcePackRegistry implements BuiltinResourcePackRegistry.Registrar {
    @Override
    public void register(Identifier resourcePack, Component displayName) {
        FabricLoader.getInstance().getModContainer(NarakaMod.MOD_ID).ifPresent(modContainer -> {
            ResourceLoader.registerBuiltinPack(
                    resourcePack,
                    modContainer,
                    displayName,
                    PackActivationType.NORMAL
            );
        });
    }
}
