package com.yummy.naraka.client.init;

import com.yummy.naraka.client.renderer.DimensionSkyRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class DimensionSkyRendererRegistry {
    private static final Map<ResourceKey<Level>, Supplier<DimensionSkyRenderer>> RENDERER_FACTORIES = new HashMap<>();
    private static final Map<ResourceKey<Level>, DimensionSkyRenderer> RENDERERS = new HashMap<>();

    public static void register(ResourceKey<Level> dimension, Supplier<DimensionSkyRenderer> factory) {
        RENDERER_FACTORIES.put(dimension, factory);
    }

    public static void setup() {
        RENDERER_FACTORIES.forEach((dimension, factory) -> RENDERERS.put(dimension, factory.get()));
    }

    public static DimensionSkyRenderer get(ResourceKey<Level> dimension) {
        return RENDERERS.getOrDefault(dimension, DimensionSkyRenderer.EMPTY);
    }
}
