package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.BlockEntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class FabricBlockEntityRendererRegistry implements BlockEntityRendererRegistry.Registrar {
    @Override
    public <T extends BlockEntity> void register(Supplier<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T> rendererProvider) {
        BlockEntityRenderers.register(blockEntity.get(), rendererProvider);
    }
}
