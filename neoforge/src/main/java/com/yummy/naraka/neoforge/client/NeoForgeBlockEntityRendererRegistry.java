package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.BlockEntityRendererRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import com.yummy.naraka.proxy.MethodProxy;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class NeoForgeBlockEntityRendererRegistry implements NarakaEventBus {
    @MethodProxy(BlockEntityRendererRegistry.class)
    public static <T extends BlockEntity> void register(Supplier<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T> rendererProvider) {
        NARAKA_BUS.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> {
            event.registerBlockEntityRenderer(blockEntity.get(), rendererProvider);
        });
    }
}
