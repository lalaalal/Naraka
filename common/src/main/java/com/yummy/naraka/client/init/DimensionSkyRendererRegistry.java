package com.yummy.naraka.client.init;

import com.yummy.naraka.client.renderer.DimensionSkyRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public abstract class DimensionSkyRendererRegistry {
    private static final Map<ResourceKey<Level>, DimensionSkyRendererFactory> RENDERER_FACTORIES = new HashMap<>();
    private static final Map<ResourceKey<Level>, DimensionSkyRenderer> RENDERERS = new HashMap<>();

    public static void register(ResourceKey<Level> dimension, DimensionSkyRendererFactory factory) {
        RENDERER_FACTORIES.put(dimension, factory);
    }

    public static void setup(TextureManager textureManager, AtlasManager atlasManager) {
        RENDERER_FACTORIES.forEach((dimension, factory) -> RENDERERS.put(dimension, factory.create(textureManager, atlasManager)));
    }

    public static void close() {
        for (DimensionSkyRenderer dimensionSkyRenderer : RENDERERS.values())
            dimensionSkyRenderer.close();
    }

    public static DimensionSkyRenderer get(ResourceKey<Level> dimension) {
        return RENDERERS.getOrDefault(dimension, DimensionSkyRenderer.EMPTY);
    }

    public interface DimensionSkyRendererFactory {
        DimensionSkyRenderer create(TextureManager textureManager, AtlasManager atlasManager);
    }
}
