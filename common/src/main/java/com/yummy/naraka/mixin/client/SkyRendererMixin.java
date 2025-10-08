package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {
    @Shadow
    @Final
    private RenderSystem.AutoStorageIndexBuffer quadIndices;

    @Nullable
    @Unique
    private AbstractTexture naraka$eclipseTexture;

    @Nullable
    @Unique
    private GpuBuffer naraka$eclipseBuffer;

    @Shadow
    protected abstract AbstractTexture getTexture(ResourceLocation resourceLocation);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SkyRenderer;buildMoonPhases()Lcom/mojang/blaze3d/buffers/GpuBuffer;"))
    private void initEclipseBuffer(CallbackInfo ci) {
        naraka$eclipseBuffer = naraka$buildEclipseBuffer();
        HerobrineSkyRenderHelper.setEclipseRenderer(this::naraka$renderEclipse);
    }

    @Inject(method = "initTextures", at = @At("TAIL"))
    public void initEclipseTexture(CallbackInfo ci) {
        naraka$eclipseTexture = getTexture(NarakaTextures.ECLIPSE);
    }

    @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
    public void replaceMoon(int i, float f, PoseStack poseStack, CallbackInfo ci) {
        if (NarakaClientContext.ENABLE_HEROBRINE_SKY.getValue() && NarakaClientContext.SHADER_ENABLED.getValue()) {
            ci.cancel();
            naraka$renderEclipse(f, poseStack);
        }
    }

    @Unique
    private GpuBuffer naraka$buildEclipseBuffer() {
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = new Matrix4f();
            bufferBuilder.addVertex(matrix4f, -1.0F, 0.0F, 1.0F).setUv(0.0F, 1.0F);
            bufferBuilder.addVertex(matrix4f, 1.0F, 0.0F, 1.0F).setUv(1.0F, 1.0F);
            bufferBuilder.addVertex(matrix4f, 1.0F, 0.0F, -1.0F).setUv(1.0F, 0.0F);
            bufferBuilder.addVertex(matrix4f, -1.0F, 0.0F, -1.0F).setUv(0.0F, 0.0F);

            try (MeshData meshData = bufferBuilder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Eclipse quad", 40, meshData.vertexBuffer());
            }
        }
    }

    @Unique
    private void naraka$renderEclipse(float rainBrightness, PoseStack poseStack) {
        if (this.naraka$eclipseTexture != null && this.naraka$eclipseBuffer != null) {
            Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
            matrix4fStack.pushMatrix();
            matrix4fStack.mul(poseStack.last().pose());
            matrix4fStack.translate(0.0F, -100.0F, 0.0F);
            matrix4fStack.scale(30, 1, 30);
            GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms()
                    .writeTransform(matrix4fStack, new Vector4f(1.0F, 1.0F, 1.0F, rainBrightness), new Vector3f(), new Matrix4f(), 0.0F);
            GpuTextureView colorTextureView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
            GpuTextureView depthTextureView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
            GpuBuffer gpuBuffer = this.quadIndices.getBuffer(6);

            if (colorTextureView != null) {
                try (RenderPass renderPass = RenderSystem.getDevice()
                        .createCommandEncoder()
                        .createRenderPass(() -> "Sky eclipse", colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
                    renderPass.setPipeline(RenderPipelines.CELESTIAL);
                    RenderSystem.bindDefaultUniforms(renderPass);
                    renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
                    renderPass.bindSampler("Sampler0", this.naraka$eclipseTexture.getTextureView());
                    renderPass.setVertexBuffer(0, this.naraka$eclipseBuffer);
                    renderPass.setIndexBuffer(gpuBuffer, this.quadIndices.type());
                    renderPass.drawIndexed(0, 0, 6, 1);
                }
            }

            matrix4fStack.popMatrix();
        }
    }

    @Inject(method = "close", at = @At("TAIL"))
    private void closeEclipseBuffer(CallbackInfo ci) {
        if (naraka$eclipseBuffer != null)
            naraka$eclipseBuffer.close();
    }
}
