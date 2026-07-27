package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.init.DimensionSkyRendererRegistry;
import com.yummy.naraka.client.renderer.DimensionTypeProvider;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    private SkyRenderer skyRenderer;

    @Shadow
    @Final
    private CloudRenderer cloudRenderer;

    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Shadow
    @Final
    private LevelTargetBundle targets;

    @Inject(method = "addSkyPass", at = @At("RETURN"))
    private void renderDimensionSky(FrameGraphBuilder frame, CameraRenderState cameraState, GpuBufferSlice skyFog, CallbackInfo ci) {
        if (levelRenderState.skyRenderState instanceof DimensionTypeProvider dimensionTypeProvider) {
            DimensionSkyRendererRegistry.get(dimensionTypeProvider.naraka$getDimensionType())
                    .renderSky(levelRenderState, targets, frame, cameraState, skyFog, skyRenderer);
        }
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyArg(
            method = {"addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V", "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Matrix4fc;)V"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V")
    )
    public Runnable replaceHerobrineSkyPass(Runnable original,
                                            @Local(argsOnly = true, name = "skyFog") GpuBufferSlice skyFog,
                                            @Local(name = "state") SkyRenderState state) {
        if (state.skybox == DimensionType.Skybox.OVERWORLD && naraka$isHerobrineSkyEnabled())
            return () -> HerobrineSkyRenderHelper.renderHerobrineSky(skyRenderer, skyFog);
        return original;
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyArg(
            method = {"addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;JFIFI)V", "addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;JFIFILorg/joml/Matrix4fc;)V"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V")
    )
    public Runnable speedUpClouds(Runnable original,
                                  @Local(argsOnly = true, name = "cloudStatus") CloudStatus cloudStatus,
                                  @Local(argsOnly = true, name = "cameraPosition") Vec3 cameraPosition,
                                  @Local(argsOnly = true, name = "partialTicks") float partialTicks,
                                  @Local(argsOnly = true, name = "cloudHeight") float cloudHeight,
                                  @Local(argsOnly = true, name = "cloudRange") int cloudRange,
                                  @Local(argsOnly = true, name = "gameTime") long gameTime) {
        if (naraka$isHerobrineSkyEnabled()) {
            int speed = NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
            return () -> this.cloudRenderer.render(ARGB.white(0.8f), cloudStatus, cloudHeight, cloudRange, cameraPosition, gameTime * speed, partialTicks * speed);
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
