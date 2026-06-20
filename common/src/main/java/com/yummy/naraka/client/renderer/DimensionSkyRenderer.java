package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;

public interface DimensionSkyRenderer extends AutoCloseable {
    DimensionSkyRenderer EMPTY = new DimensionSkyRenderer() {
        @Override
        public void renderSky(LevelRenderState level, LevelTargetBundle targets, FrameGraphBuilder frameGraphBuilder, CameraRenderState camera, GpuBufferSlice shaderFog, SkyRenderer skyRenderer) {
        }

        @Override
        public void close() {
        }
    };

    static AbstractTexture getTexture(Identifier location) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        return textureManager.getTexture(location);
    }

    void renderSky(LevelRenderState level, LevelTargetBundle targets, FrameGraphBuilder frameGraphBuilder, CameraRenderState camera, GpuBufferSlice shaderFog, SkyRenderer skyRenderer);

    @Override
    void close();
}
