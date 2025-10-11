package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.BlockEntityRendererRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricBlockEntityRendererRegistry {
    @MethodProxy(BlockEntityRendererRegistry.class)
    public static <T extends BlockEntity, S extends BlockEntityRenderState> void register(Supplier<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T, S> rendererProvider) {
        BlockEntityRenderers.register(blockEntity.get(), rendererProvider);
    }
}
