package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.EntityRendererRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

public final class NeoForgeEntityRendererRegistry implements EntityRendererRegistry.Registrar, NarakaEventBus {
    @Override
    public <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider) {
        NARAKA_BUS.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> {
            event.registerEntityRenderer(entity.get(), rendererProvider);
        });
    }
}
