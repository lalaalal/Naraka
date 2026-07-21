package com.yummy.naraka.client.init;

import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class BlockEntityRendererRegistry {
    public static <T extends BlockEntity> void register(ValueGetter<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T> rendererProvider) {
        MethodInvoker.invoke(BlockEntityRendererRegistry.class, "register", blockEntity, rendererProvider);
    }
}
