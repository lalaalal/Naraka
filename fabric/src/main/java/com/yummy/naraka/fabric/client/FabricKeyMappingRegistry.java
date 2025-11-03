package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.KeyMappingRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

@Environment(EnvType.CLIENT)
public final class FabricKeyMappingRegistry {
    @MethodProxy(KeyMappingRegistry.class)
    public static void register(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }
}
