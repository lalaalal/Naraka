package com.yummy.naraka.client.init;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.mixin.accessor.SpecialModelRenderersAccessor;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SpecialModelRendererRegistry {
    private static final Map<Block, SpecialModelRenderer.Unbaked> STATIC_MAPPING = new HashMap<>();

    public static void registerCodecId(ResourceLocation location, MapCodec<? extends SpecialModelRenderer.Unbaked> codec) {
        SpecialModelRenderersAccessor.getIdMapper().put(location, codec);
    }

    public static void register(Block block, SpecialModelRenderer.Unbaked unbaked) {
        STATIC_MAPPING.put(block, unbaked);
    }

    public static void forEachUnbaked(BiConsumer<Block, SpecialModelRenderer.Unbaked> consumer) {
        STATIC_MAPPING.forEach(consumer);
    }
}
