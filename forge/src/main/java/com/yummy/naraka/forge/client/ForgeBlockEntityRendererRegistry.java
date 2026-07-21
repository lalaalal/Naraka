package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.BlockEntityRendererRegistry;
import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ForgeBlockEntityRendererRegistry implements NarakaEventBus {
    @MethodProxy(BlockEntityRendererRegistry.class)
    public static <T extends BlockEntity> void register(ValueGetter<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T> rendererProvider) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterRenderers.class, event -> {
            event.registerBlockEntityRenderer(blockEntity.getConcreteValue(), rendererProvider);
        });
    }
}
