package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.BlockEntityRendererRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
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
    public static <T extends BlockEntity, S extends BlockEntityRenderState> void register(Supplier<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T, S> rendererProvider) {
        NARAKA_BUS.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> {
            event.registerBlockEntityRenderer(blockEntity.get(), rendererProvider);
        });
    }
}
