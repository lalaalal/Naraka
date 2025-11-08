package com.yummy.naraka.neoforge.mixin.client;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.yummy.naraka.client.init.DimensionSkyRendererRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow @Nullable
    private ClientLevel level;

    @Shadow @Final
    private LevelTargetBundle targets;

    @Shadow @Final
    LevelRenderState levelRenderState;

    @Inject(
            method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Matrix4f;)V",
            at = @At("RETURN")
    )
    private void renderDimensionSky(FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice shaderFog, Matrix4f modelViewMatrix, CallbackInfo ci) {
        if (level == null)
            return;
        DimensionSkyRendererRegistry.get(level.dimension())
                .renderSky(level, targets, frameGraphBuilder, camera, shaderFog, levelRenderState.skyRenderState);
    }
}
