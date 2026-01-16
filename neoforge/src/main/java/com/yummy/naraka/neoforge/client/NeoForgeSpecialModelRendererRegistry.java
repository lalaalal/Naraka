package com.yummy.naraka.neoforge.client;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.init.SpecialModelRendererRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterSpecialBlockModelRendererEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class NeoForgeSpecialModelRendererRegistry implements NarakaEventBus {
    @MethodProxy(SpecialModelRendererRegistry.class)
    public static void registerCodecId(Identifier location, MapCodec<? extends SpecialModelRenderer.Unbaked> codec) {
        NARAKA_BUS.addListener(RegisterSpecialModelRendererEvent.class, event -> {
            event.register(location, codec);
        });
    }

    @MethodProxy(SpecialModelRendererRegistry.class)
    public static void registerBlock(Supplier<? extends Block> block, SpecialModelRenderer.Unbaked unbaked) {
        NARAKA_BUS.addListener(RegisterSpecialBlockModelRendererEvent.class, event -> {
            event.register(block.get(), unbaked);
        });
    }
}
