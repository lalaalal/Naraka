package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public abstract class EntityRendererRegistry {
    public static <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider) {
        MethodInvoker.invoke(EntityRendererRegistry.class, "register", entity, rendererProvider);
    }
}
