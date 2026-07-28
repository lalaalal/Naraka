package com.yummy.naraka.client.init;

import com.yummy.naraka.client.service.NarakaClientServices;
import com.yummy.naraka.core.registries.ValueGetter;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class BlockEntityRendererRegistry {
    public static <T extends BlockEntity> void register(ValueGetter<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T> rendererProvider) {
        NarakaClientServices.BLOCK_ENTITY_RENDERER_REGISTRY.register(blockEntity, rendererProvider);
    }

    public interface Registrar {
        <T extends BlockEntity> void register(ValueGetter<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T> rendererProvider);
    }
}
