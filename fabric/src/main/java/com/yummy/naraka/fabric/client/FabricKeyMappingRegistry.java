package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.KeyMappingRegistry;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public final class FabricKeyMappingRegistry implements KeyMappingRegistry.Registrar {
    @Override
    public void register(KeyMapping keyMapping) {
        KeyMappingHelper.registerKeyMapping(keyMapping);
    }
}
