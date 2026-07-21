package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.EntityRendererRegistry;
import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ForgeEntityRendererRegistry implements NarakaEventBus {
    @MethodProxy(EntityRendererRegistry.class)
    public static <T extends Entity> void register(ValueGetter<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterRenderers.class, event -> {
            event.registerEntityRenderer(entity.getConcreteValue(), rendererProvider);
        });
    }
}
