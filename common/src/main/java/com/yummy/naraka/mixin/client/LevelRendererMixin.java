package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.init.DimensionSkyRendererRegistry;
import com.yummy.naraka.client.renderer.NarakaSkyRenderer;
import com.yummy.naraka.config.NarakaConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    private @Nullable ClientLevel level;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void prepareDimensionSkyRenderers(Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, RenderBuffers renderBuffers, CallbackInfo ci) {
        DimensionSkyRendererRegistry.setup();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (NarakaClientContext.TICK_FROZEN.getValue())
            ci.cancel();
    }

    @ModifyVariable(method = "renderClouds", at = @At(value = "STORE"), ordinal = 4)
    private double speedUpClouds(double e) {
        if (naraka$isHerobrineSkyEnabled())
            return e * NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
        return e;
    }

    @ModifyArg(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FDDD)V"))
    private float fixCloudPartialTickOnTickFreeze(float partialTicks) {
        if (NarakaClientContext.TICK_FROZEN.getValue())
            return NarakaClientContext.FROZEN_PARTIAL_TICK.getValue();
        return partialTicks;
    }

    @ModifyArg(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"))
    private float fixEntityPartialTickOnTickFreeze(float partialTicks) {
        if (NarakaClientContext.TICK_FROZEN.getValue())
            return NarakaClientContext.FROZEN_PARTIAL_TICK.getValue();
        return partialTicks;
    }

    @ModifyExpressionValue(method = "renderClouds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getCloudColor(F)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 modifyCloudColor(Vec3 original) {
        if (naraka$isHerobrineSkyEnabled())
            return new Vec3(1, 1, 1);
        return original;
    }

    @ModifyExpressionValue(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I"))
    private int modifyMoonPhase(int original) {
        if (naraka$isHerobrineSkyEnabled())
            return 4;
        return original;
    }

    @ModifyExpressionValue(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F"))
    private float modifyStarBrightness(float original) {
        if (naraka$isHerobrineSkyEnabled())
            return 0;
        return original;
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F"))
    private void renderEclipse(PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        if (naraka$isHerobrineSkyEnabled()) {
            NarakaSkyRenderer.renderEclipse(poseStack, Tesselator.getInstance(), NarakaTextures.ECLIPSE);
        }
    }

    @Inject(method = "renderSky", at = @At("RETURN"))
    private void renderDimensionSky(PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        if (level == null)
            return;
        DimensionSkyRendererRegistry.get(level.dimension())
                .renderSky(poseStack, level, projectionMatrix, partialTick, camera, isFoggy, skyFogSetup);
    }

    @Unique
    private static boolean naraka$isHerobrineSkyEnabled() {
        return NarakaClientContext.ENABLE_HEROBRINE_SKY.getValue();
    }
}
