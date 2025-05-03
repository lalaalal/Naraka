package com.yummy.naraka.client.init;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class SpecialModelRendererRegistry {
    public static void registerCodecId(ResourceLocation location, MapCodec<? extends SpecialModelRenderer.Unbaked> codec) {
        MethodInvoker.of(SpecialModelRendererRegistry.class, "registerCodecId")
                .invoke(location, codec);
    }

    public static void registerBlock(Supplier<? extends Block> block, SpecialModelRenderer.Unbaked unbaked) {
        MethodInvoker.of(SpecialModelRendererRegistry.class, "registerBlock")
                .invoke(block, unbaked);
    }
}
