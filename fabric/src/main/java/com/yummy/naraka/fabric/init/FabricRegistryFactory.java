package com.yummy.naraka.fabric.init;

import com.yummy.naraka.core.registries.RegistryFactory;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class FabricRegistryFactory extends RegistryFactory {
    public static final FabricRegistryFactory INSTANCE = new FabricRegistryFactory();

    private FabricRegistryFactory() {

    }

    @Override
    public <T> Registry<T> createSimple(ResourceKey<Registry<T>> key) {
        return FabricRegistryBuilder.createSimple(key)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister();
    }
}