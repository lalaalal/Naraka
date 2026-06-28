package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public interface DimensionSkyRenderer {
    DimensionSkyRenderer EMPTY = (level, projectionMatrix, partialTick, camera, isFoggy, skyFogSetup) -> {
    };

    static VertexBuffer createBuffer(BufferBuilder.RenderedBuffer renderedBuffer) {
        RenderSystem.setShader(GameRenderer::getPositionShader);

        VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        vertexBuffer.bind();
        vertexBuffer.upload(renderedBuffer);
        VertexBuffer.unbind();

        return vertexBuffer;
    }

    void renderSky(ClientLevel level, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup);
}
