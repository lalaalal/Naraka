package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class FabricEntityRendererRegistry implements EntityRendererRegistry.Registrar {
    @Override
    public <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider) {
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(entity.get(), rendererProvider);
    }
}
