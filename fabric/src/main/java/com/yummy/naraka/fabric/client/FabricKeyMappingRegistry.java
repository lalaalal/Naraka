package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.KeyMappingRegistry;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public final class FabricKeyMappingRegistry implements KeyMappingRegistry.Registrar {
    @Override
    public void register(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }
}
