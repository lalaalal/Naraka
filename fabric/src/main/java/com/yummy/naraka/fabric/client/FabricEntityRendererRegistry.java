package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public final class FabricEntityRendererRegistry implements EntityRendererRegistry.Registrar {
    @Override
    public <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider) {
        EntityRenderers.register(entity.get(), rendererProvider);
    }
}
