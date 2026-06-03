package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaTextures;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class NarakaSkyRenderer implements DimensionSkyRenderer {
    @Nullable
    private static NarakaSkyRenderer instance;

    private final RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);

    private final GpuBuffer eclipseBuffer = buildEclipse();
    private final Map<Identifier, AbstractTexture> eclipseTextures;

    public static NarakaSkyRenderer getInstance() {
        if (instance == null)
            throw new IllegalStateException("Naraka sky renderer is not initialized");
        return instance;
    }

    public NarakaSkyRenderer() {
        if (instance != null)
            throw new IllegalStateException("Naraka sky renderer already initialized");
        instance = this;

        AbstractTexture eclipseTexture = DimensionSkyRenderer.getTexture(NarakaTextures.ECLIPSE);
        AbstractTexture invertedEclipseTexture = DimensionSkyRenderer.getTexture(NarakaTextures.INVERTED_ECLIPSE);
        eclipseTextures = Map.of(
                NarakaTextures.ECLIPSE, eclipseTexture,
                NarakaTextures.INVERTED_ECLIPSE, invertedEclipseTexture
        );
    }

    private GpuBuffer buildEclipse() {
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = new Matrix4f();
            bufferBuilder.addVertex(matrix4f, -1, 0, -1).setUv(0, 1);
            bufferBuilder.addVertex(matrix4f, 1, 0, -1).setUv(1, 1);
            bufferBuilder.addVertex(matrix4f, 1, 0, 1).setUv(1, 0);
            bufferBuilder.addVertex(matrix4f, -1, 0, 1).setUv(0, 0);

            try (MeshData meshData = bufferBuilder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Eclipse quad", 40, meshData.vertexBuffer());
            }
        }
    }

    @Override
    public void renderSky(ClientLevel level, LevelTargetBundle targets, FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice shaderFog, SkyRenderer skyRenderer, SkyRenderState renderState) {
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
            renderEclipse(poseStack, NarakaTextures.ECLIPSE, RenderPipelines.CELESTIAL);
            poseStack.popPose();
        });
    }

    public void renderEclipse(PoseStack poseStack, Identifier textureIdentifier, RenderPipeline renderPipeline) {
        if (!eclipseTextures.containsKey(textureIdentifier))
            return;
        AbstractTexture texture = eclipseTextures.get(textureIdentifier);
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.mul(poseStack.last().pose());
        matrix4fStack.translate(0, 75, 0);
        matrix4fStack.scale(30, 1, 30);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms()
                .writeTransform(matrix4fStack, new Vector4f(1, 1, 1, 1), new Vector3f(), new Matrix4f());
        GpuTextureView colorTextureView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
        GpuTextureView depthTextureView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
        GpuBuffer gpuBuffer = this.quadIndices.getBuffer(6);

        if (colorTextureView != null) {
            try (RenderPass renderPass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Sky eclipse", colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
                renderPass.setPipeline(renderPipeline);
                RenderSystem.bindDefaultUniforms(renderPass);
                renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
                renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
                renderPass.setVertexBuffer(0, this.eclipseBuffer);
                renderPass.setIndexBuffer(gpuBuffer, this.quadIndices.type());
                renderPass.drawIndexed(0, 0, 6, 1);
            }
        }

        matrix4fStack.popMatrix();
    }

    @Override
    public void close() {
        instance = null;
        eclipseBuffer.close();
    }
}
