package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;

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
        FramePass framePass = frameGraphBuilder.addPass("naraka sky");
        targets.main = framePass.readsAndWrites(targets.main);
        framePass.executes(() -> {
            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(-90));

            if (NarakaClientContext.SHADER_ENABLED.getValue()) {
                RenderSystem.setShaderFog(shaderFog);
                skyRenderer.renderSkyDisc(ARGB.white(0xff));
            }
            renderEclipse(poseStack);
            poseStack.popPose();
        });
    }

    public void renderEclipse(PoseStack poseStack) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul(poseStack.last().pose());
        modelViewStack.translate(0, 75, 0);
        modelViewStack.scale(30, 1, 30);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms()
                .writeTransform(new Matrix4f(modelViewStack), new Vector4f(1, 1, 1, 1));
        GpuTextureView colorTextureView = renderTarget.getColorTextureView();
        GpuTextureView depthTextureView = renderTarget.getDepthTextureView();
        GpuBuffer gpuBuffer = quadIndices.getBuffer(6);

        if (colorTextureView != null) {
            try (RenderPass renderPass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Sky eclipse", colorTextureView, Optional.empty(), depthTextureView, OptionalDouble.empty())) {
                renderPass.setPipeline(RenderPipelines.CELESTIAL);
                RenderSystem.bindDefaultUniforms(renderPass);
                renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
                renderPass.bindTexture("Sampler0", eclipseTexture.getTextureView(), eclipseTexture.getSampler());
                renderPass.setVertexBuffer(0, this.eclipseBuffer.slice());
                renderPass.setIndexBuffer(gpuBuffer, this.quadIndices.type());
                renderPass.drawIndexed(6, 1, 0, 0, 0);
            }
        }

        modelViewStack.popMatrix();
    }

    @Override
    public void close() {
        instance = null;
        eclipseBuffer.close();
    }
}
