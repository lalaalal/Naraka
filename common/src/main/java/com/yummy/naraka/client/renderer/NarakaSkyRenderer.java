package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalDouble;

public class NarakaSkyRenderer implements DimensionSkyRenderer {
    @Nullable
    private static NarakaSkyRenderer instance;

    private final RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);

    private final GpuBuffer eclipseBuffer = buildEclipse();
    private final AbstractTexture eclipseTexture = DimensionSkyRenderer.getTexture(NarakaTextures.ECLIPSE);
    private final RenderTarget renderTarget = Minecraft.getInstance().gameRenderer.mainRenderTarget();

    public static NarakaSkyRenderer getInstance() {
        if (instance == null)
            throw new IllegalStateException("Naraka sky renderer is not initialized");
        return instance;
    }

    public NarakaSkyRenderer() {
        if (instance != null)
            throw new IllegalStateException("Naraka sky renderer already initialized");
        instance = this;
    }

    private GpuBuffer buildEclipse() {
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = new Matrix4f();
            bufferBuilder.addVertex(matrix4f, -1, 0, -1).setUv(0, 1);
            bufferBuilder.addVertex(matrix4f, 1, 0, -1).setUv(1, 1);
            bufferBuilder.addVertex(matrix4f, 1, 0, 1).setUv(1, 0);
            bufferBuilder.addVertex(matrix4f, -1, 0, 1).setUv(0, 0);

            try (MeshData meshData = bufferBuilder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Eclipse quad", 36, meshData.vertexBuffer());
            }
        }
    }

    @Override
    public void renderSky(LevelRenderState level, LevelTargetBundle targets, FrameGraphBuilder frameGraphBuilder, CameraRenderState camera, GpuBufferSlice shaderFog, SkyRenderer skyRenderer) {
        GpuTextureView colorTextureView = renderTarget.getColorTextureView();
        GpuTextureView depthTextureView = renderTarget.getDepthTextureView();
        if (colorTextureView == null || depthTextureView == null)
            return;
        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky eclipse", colorTextureView, Optional.empty(), depthTextureView, OptionalDouble.empty())) {
            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            poseStack.rotate(Axis.YP.rotationDegrees(-90));

            if (NarakaClientContext.SHADER_ENABLED.getValue()) {
                RenderSystem.setShaderFog(shaderFog);
                skyRenderer.renderSkyDisc(renderPass, new Vector3f(1, 1, 1));
            }
            renderEclipse(renderPass, poseStack);
            poseStack.popPose();
        }
    }

    public void renderEclipse(RenderPass renderPass, PoseStack poseStack) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        modelViewStack.translate(0, 75, 0);
        modelViewStack.scale(30, 1, 30);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms()
                .writeTransform(new Matrix4f(modelViewStack), new Vector4f(1, 1, 1, 1));
        GpuBuffer gpuBuffer = quadIndices.getBuffer(6);

        renderPass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.CELESTIAL));
        RenderSystem.bindDefaultUniforms(renderPass);
        renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
        renderPass.setUniform("Sampler0", eclipseTexture.getTextureView(), eclipseTexture.getSampler());
        renderPass.setVertexBuffer(0, this.eclipseBuffer.slice());
        renderPass.setIndexBuffer(gpuBuffer, this.quadIndices.type());
        renderPass.drawIndexed(6, 1, 0, 0, 0);

        modelViewStack.popMatrix();
    }

    @Override
    public void close() {
        instance = null;
        eclipseBuffer.close();
    }
}
