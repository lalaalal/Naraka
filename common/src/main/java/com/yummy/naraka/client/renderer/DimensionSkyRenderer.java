package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Matrix4f;

public interface DimensionSkyRenderer {
    DimensionSkyRenderer EMPTY = (level, frustumMatrix, projectionMatrix, partialTick, camera, isFoggy, skyFogSetup) -> {
    };

    static VertexBuffer createBuffer(MeshData meshData) {
        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        buffer.bind();
        buffer.upload(meshData);
        VertexBuffer.unbind();
        return buffer;
    }

    void renderSky(ClientLevel level, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup);
}
