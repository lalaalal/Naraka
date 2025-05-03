package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class BlockEntityRendererRegistry {
    public static <T extends BlockEntity> void register(Supplier<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T> rendererProvider) {
        MethodInvoker.invoke(BlockEntityRendererRegistry.class, "register", blockEntity, rendererProvider);
    }
}
