package com.yummy.naraka.client.init;

import com.yummy.naraka.client.service.NarakaClientServices;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public abstract class EntityRendererRegistry {
    public static <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider) {
        NarakaClientServices.ENTITY_RENDERER_REGISTRY.register(entity, rendererProvider);
    }

    public interface Registrar {
        <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider);
    }
}
