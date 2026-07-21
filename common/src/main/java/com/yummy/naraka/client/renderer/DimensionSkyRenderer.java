package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public interface DimensionSkyRenderer {
    DimensionSkyRenderer EMPTY = (poseStack, level, projectionMatrix, partialTick, camera, isFoggy, skyFogSetup) -> {
    };

    static VertexBuffer createBuffer(BufferBuilder.RenderedBuffer renderedBuffer) {
        RenderSystem.setShader(GameRenderer::getPositionShader);

        VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        vertexBuffer.bind();
        vertexBuffer.upload(renderedBuffer);
        VertexBuffer.unbind();

        return vertexBuffer;
    }

    void renderSky(PoseStack poseStack, ClientLevel level, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup);
}
