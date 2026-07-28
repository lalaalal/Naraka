package com.yummy.naraka.fabric.init;

import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryReader;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class FabricRegistryFactory implements RegistryFactory {
    @Override
    public <T> RegistryReader<T> createSimple(ResourceKey<Registry<T>> key) {
        return new RegistryReader.Simple<>(FabricRegistryBuilder.createSimple(key)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister()
        );
    }
}
