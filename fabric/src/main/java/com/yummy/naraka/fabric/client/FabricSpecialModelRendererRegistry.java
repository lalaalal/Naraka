package com.yummy.naraka.fabric.client;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.init.SpecialModelRendererRegistry;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public final class FabricSpecialModelRendererRegistry implements SpecialModelRendererRegistry.Registrar {
    @Override
    public void registerCodecId(Identifier location, MapCodec<? extends SpecialModelRenderer.Unbaked<?>> codec) {
        SpecialModelRenderers.ID_MAPPER.put(location, codec);
    }

    @Override
    public void registerBlock(Supplier<? extends Block> block, SpecialModelRenderer.Unbaked<?> unbaked) {

    }
}
