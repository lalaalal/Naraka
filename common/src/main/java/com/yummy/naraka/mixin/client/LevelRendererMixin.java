package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.init.DimensionSkyRendererRegistry;
import com.yummy.naraka.client.renderer.DimensionTypeProvider;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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

    @Shadow
    @Final
    private GameRenderer gameRenderer;

    @Inject(
            method = "addSkyPass",
            at = @At("RETURN")
    )
    private void renderDimensionSky(FrameGraphBuilder frame, CameraRenderState cameraState, GpuBufferSlice skyFog, CallbackInfo ci) {
        if (levelRenderState.skyRenderState instanceof DimensionTypeProvider dimensionTypeProvider) {
            FramePass framePass = frame.addPass("custom dimension sky");
            targets.main = framePass.readsAndWrites(targets.main);
            framePass.executes(() -> {
                DimensionSkyRendererRegistry.get(dimensionTypeProvider.naraka$getDimensionType())
                        .renderSky(levelRenderState, targets, frame, cameraState, skyFog, skyRenderer);
            });
        }
    }

    @ModifyArg(
            method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V")
    )
    public Runnable replaceHerobrineSkyPass(Runnable original,
                                            @Local(argsOnly = true, name = "skyFog") GpuBufferSlice skyFog) {
        if (levelRenderState.skyRenderState.skybox == DimensionType.Skybox.OVERWORLD && naraka$isHerobrineSkyEnabled())
            return () -> HerobrineSkyRenderHelper.renderHerobrineSky(gameRenderer.mainRenderTarget(), skyRenderer, skyFog);
        return original;
    }

    @ModifyArgs(
            method = "prepareTranslucents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CloudRenderer;prepare(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V")
    )
    public void speedUpClouds(Args args) {
        long gameTime = args.get(5);
        float partialTicks = args.get(6);
        if (naraka$isHerobrineSkyEnabled()) {
            int speed = NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
            args.set(5, gameTime * speed);
            args.set(6, partialTicks * speed);
        }
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
