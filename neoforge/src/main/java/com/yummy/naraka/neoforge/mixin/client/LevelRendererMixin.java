package com.yummy.naraka.neoforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow @Final
    private SkyRenderer skyRenderer;
    @Shadow @Final
    private RenderBuffers renderBuffers;

    @Shadow @Final
    private CloudRenderer cloudRenderer;

    @Inject(method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/FogParameters;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V"), cancellable = true)
    public void replaceHerobrineSkyPass(FrameGraphBuilder arg, Camera arg2, float f, FogParameters fog, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local DimensionSpecialEffects.SkyType skyType, @Local FramePass framePass) {
        if (skyType == DimensionSpecialEffects.SkyType.OVERWORLD && NarakaConfig.CLIENT.renderHerobrineSky.getValue()) {
            ci.cancel();
            framePass.executes(() -> HerobrineSkyRenderHelper.renderHerobrineSky(skyRenderer, renderBuffers, fog));
        }
    }

    @Inject(method = "addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;FIFLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V"), cancellable = true)
    public void speedUpClouds(FrameGraphBuilder arg, CloudStatus cloudStatus, Vec3 cameraPosition, float ticks, int cloudColor, float height, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local FramePass framePass) {
        if (NarakaConfig.CLIENT.renderHerobrineSky.getValue()) {
            int speed = NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
            ci.cancel();
            framePass.executes(() -> this.cloudRenderer.render(cloudColor, cloudStatus, height, cameraPosition, ticks * speed));
        }
    }
}
