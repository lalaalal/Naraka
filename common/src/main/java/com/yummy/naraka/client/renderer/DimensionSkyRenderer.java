package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;

@Environment(EnvType.CLIENT)
public interface DimensionSkyRenderer extends AutoCloseable {
    DimensionSkyRenderer EMPTY = new DimensionSkyRenderer() {
        @Override
        public void renderSky(ClientLevel level, LevelTargetBundle targets, FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice shaderFog, SkyRenderer skyRenderer, SkyRenderState renderState) {
        }

        @Override
        public void close() {
        }
    };

    void renderSky(ClientLevel level, LevelTargetBundle targets, FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice shaderFog, SkyRenderer skyRenderer, SkyRenderState renderState);

    @Override
    void close();
}
