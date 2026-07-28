package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.BlockEntityRendererRegistry;
import com.yummy.naraka.core.registries.ValueGetter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Environment(EnvType.CLIENT)
public final class FabricBlockEntityRendererRegistry implements BlockEntityRendererRegistry.Registrar {
    @Override
    public <T extends BlockEntity> void register(ValueGetter<? extends BlockEntityType<? extends T>> blockEntity, BlockEntityRendererProvider<T> rendererProvider) {
        BlockEntityRenderers.register(blockEntity.getConcreteValue(), rendererProvider);
    }
}
