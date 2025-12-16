package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.init.DimensionSkyRendererRegistry;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import com.yummy.naraka.config.NarakaConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    private SkyRenderer skyRenderer;

    @Shadow
    @Final
    private CloudRenderer cloudRenderer;

    @Shadow private @Nullable ClientLevel level;

    @Shadow @Final private LevelRenderState levelRenderState;

    @Shadow @Final private LevelTargetBundle targets;

    @Inject(method = "onResourceManagerReload", at = @At("RETURN"))
    private void prepareDimensionSkyRenderers(ResourceManager resourceManager, CallbackInfo ci) {
        DimensionSkyRendererRegistry.setup();
    }

    /**
     * @see com.yummy.naraka.neoforge.mixin.client.LevelRendererMixin
     */
    @Inject(
            method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
            at = @At("RETURN")
    )
    private void renderDimensionSky(FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice shaderFog, CallbackInfo ci) {
        if (level == null)
            return;
        DimensionSkyRendererRegistry.get(level.dimension())
                .renderSky(level, targets, frameGraphBuilder, camera, shaderFog, skyRenderer, levelRenderState.skyRenderState);
    }

    @SuppressWarnings({"UnresolvedMixinReference", "UnnecessaryQualifiedMemberReference"})
    @ModifyArg(
            method = {"Lnet/minecraft/client/renderer/LevelRenderer;addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V", "Lnet/minecraft/client/renderer/LevelRenderer;addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Matrix4f;)V"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V")
    )
    public Runnable replaceHerobrineSkyPass(Runnable original, @Local(argsOnly = true) GpuBufferSlice gpuBufferSlice, @Local SkyRenderState skyRenderState) {
        if (skyRenderState.skybox == DimensionType.Skybox.OVERWORLD && naraka$isHerobrineSkyEnabled())
            return () -> HerobrineSkyRenderHelper.renderHerobrineSky(skyRenderer, skyRenderState, gpuBufferSlice);
        return original;
    }

    @SuppressWarnings({"UnresolvedMixinReference", "UnnecessaryQualifiedMemberReference"})
    @ModifyArg(
            method = {"Lnet/minecraft/client/renderer/LevelRenderer;addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;JFIF)V", "Lnet/minecraft/client/renderer/LevelRenderer;addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;JFIFLorg/joml/Matrix4f;)V"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V")
    )
    public Runnable speedUpClouds(Runnable original, @Local(argsOnly = true) CloudStatus cloudStatus, @Local(argsOnly = true) Vec3 cameraPosition, @Local(ordinal = 0, argsOnly = true) float partialTick, @Local(ordinal = 1, argsOnly = true) float cloudHeight, @Local(ordinal = 0, argsOnly = true) long gameTime) {
        if (naraka$isHerobrineSkyEnabled()) {
            int speed = NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
            return () -> this.cloudRenderer.render(ARGB.white(0.8f), cloudStatus, cloudHeight, cameraPosition, gameTime * speed, partialTick * speed);
        }
        return original;
    }

    @Inject(method = "close", at = @At("TAIL"))
    private void closeCustomSkyRenderers(CallbackInfo ci) {
        DimensionSkyRendererRegistry.close();
    }

    @Unique
    private static boolean naraka$isHerobrineSkyEnabled() {
        return NarakaClientContext.ENABLE_HEROBRINE_SKY.getValue() && !NarakaClientContext.SHADER_ENABLED.getValue();
    }
}
