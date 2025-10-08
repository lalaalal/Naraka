package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import com.yummy.naraka.config.NarakaConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    @Final
    private SkyRenderer skyRenderer;

    @Shadow
    @Final
    private CloudRenderer cloudRenderer;

    @SuppressWarnings({"UnresolvedMixinReference", "LocalMayBeArgsOnly", "UnnecessaryQualifiedMemberReference"})
    @ModifyArg(
            method = {"Lnet/minecraft/client/renderer/LevelRenderer;addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V", "Lnet/minecraft/client/renderer/LevelRenderer;addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Matrix4f;)V"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V")
    )
    public Runnable replaceHerobrineSkyPass(Runnable original, @Local(argsOnly = true) GpuBufferSlice gpuBufferSlice, @Local SkyRenderState skyRenderState) {
        if (skyRenderState.skyType == DimensionSpecialEffects.SkyType.OVERWORLD && naraka$isHerobrineSkyEnabled())
            return () -> HerobrineSkyRenderHelper.renderHerobrineSky(skyRenderer, skyRenderState, gpuBufferSlice);
        return original;
    }

    @SuppressWarnings({"UnresolvedMixinReference", "UnnecessaryQualifiedMemberReference"})
    @ModifyArg(
            method = {"addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;FIF)V", "Lnet/minecraft/client/renderer/LevelRenderer;addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;FIFLorg/joml/Matrix4f;)V"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V")
    )
    public Runnable speedUpClouds(Runnable original, @Local(argsOnly = true) CloudStatus cloudStatus, @Local(argsOnly = true) Vec3 cameraPosition, @Local(ordinal = 0, argsOnly = true) float ticks, @Local(ordinal = 1, argsOnly = true) float cloudHeight) {
        if (naraka$isHerobrineSkyEnabled()) {
            int speed = NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
            float fakeTicks = ticks * speed;
            return () -> this.cloudRenderer.render(ARGB.white(0.8f), cloudStatus, cloudHeight, cameraPosition, fakeTicks);
        }
        return original;
    }

    @Unique
    private static boolean naraka$isHerobrineSkyEnabled() {
        return NarakaClientContext.ENABLE_HEROBRINE_SKY.getValue() && !NarakaClientContext.SHADER_ENABLED.getValue();
    }
}
