package com.yummy.naraka.client.init;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.NarakaClientServices;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public abstract class SpecialModelRendererRegistry {
    public static void registerCodecId(Identifier location, MapCodec<? extends SpecialModelRenderer.Unbaked<?>> codec) {
        NarakaClientServices.SPECIAL_MODEL_RENDERER_REGISTRY.registerCodecId(location, codec);
    }

    public static void registerBlock(Supplier<? extends Block> block, SpecialModelRenderer.Unbaked<?> unbaked) {
        NarakaClientServices.SPECIAL_MODEL_RENDERER_REGISTRY.registerBlock(block, unbaked);
    }

    public interface Registrar {
        void registerCodecId(Identifier location, MapCodec<? extends SpecialModelRenderer.Unbaked<?>> codec);

        void registerBlock(Supplier<? extends Block> block, SpecialModelRenderer.Unbaked<?> unbaked);
    }
}
