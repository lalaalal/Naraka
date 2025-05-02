package com.yummy.naraka.client.init;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.mixin.accessor.SpecialModelRenderersAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class SpecialModelRendererRegistry {
    private static final Map<Block, SpecialModelRenderer.Unbaked> STATIC_MAPPING = new HashMap<>();

    public static void registerCodecId(ResourceLocation location, MapCodec<? extends SpecialModelRenderer.Unbaked> codec) {
        SpecialModelRenderersAccessor.getIdMapper().put(location, codec);
    }

    public static void registerBlock(Block block, SpecialModelRenderer.Unbaked unbaked) {
        STATIC_MAPPING.put(block, unbaked);
    }

    public static void registerBlock(Supplier<? extends Block> block, SpecialModelRenderer.Unbaked unbaked) {
        registerBlock(block.get(), unbaked);
    }

    public static void forEachUnbaked(BiConsumer<Block, SpecialModelRenderer.Unbaked> consumer) {
        STATIC_MAPPING.forEach(consumer);
    }
}
