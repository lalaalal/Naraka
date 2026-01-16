package com.yummy.naraka.fabric.init;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.init.SpecialModelRendererRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialBlockRendererRegistry;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class FabricSpecialModelRendererRegistry {
    @MethodProxy(SpecialModelRendererRegistry.class)
    public static void registerCodecId(Identifier location, MapCodec<? extends SpecialModelRenderer.Unbaked> codec) {
        SpecialModelRenderers.ID_MAPPER.put(location, codec);
    }

    @MethodProxy(SpecialModelRendererRegistry.class)
    public static void registerBlock(Supplier<? extends Block> block, SpecialModelRenderer.Unbaked unbaked) {
        SpecialBlockRendererRegistry.register(block.get(), unbaked);
    }
}
