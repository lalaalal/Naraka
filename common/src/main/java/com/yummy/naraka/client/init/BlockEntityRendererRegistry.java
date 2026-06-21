package com.yummy.naraka.client.init;

import com.yummy.naraka.client.NarakaClientServices;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public abstract class BlockEntityRendererRegistry {
    public static <T extends BlockEntity, S extends BlockEntityRenderState> void register(Supplier<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T, S> rendererProvider) {
        NarakaClientServices.BLOCK_ENTITY_RENDERER_REGISTRY.register(blockEntity, rendererProvider);
    }

    public interface Registrar {
        <T extends BlockEntity, S extends BlockEntityRenderState> void register(Supplier<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T, S> rendererProvider);
    }
}
