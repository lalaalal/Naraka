package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow private @Nullable ClientLevel level;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void prepareDimensionSkyRenderers(Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, RenderBuffers renderBuffers, CallbackInfo ci) {
        DimensionSkyRendererRegistry.setup();
    }

    @ModifyVariable(method = "renderClouds", at = @At(value = "STORE"), ordinal = 4)
    private static double speedUpClouds(double original) {
        if (naraka$isHerobrineSkyEnabled())
            return original * NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
        return original;
    }

    @ModifyExpressionValue(method = "renderClouds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getCloudColor(F)Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 modifyCloudColor(Vec3 original) {
        if (naraka$isHerobrineSkyEnabled())
            return new Vec3(1, 1, 1);
        return original;
    }

    @ModifyExpressionValue(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I"))
    private static int modifyMoonPhase(int original) {
        if (naraka$isHerobrineSkyEnabled())
            return 4;
        return original;
    }

    @ModifyExpressionValue(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F"))
    private static float modifyStarBrightness(float original) {
        if (naraka$isHerobrineSkyEnabled())
            return 0;
        return original;
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F"))
    private void renderEclipse(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci, @Local Tesselator tesselator, @Local PoseStack poseStack) {
        if (naraka$isHerobrineSkyEnabled()) {
            NarakaSkyRenderer.renderEclipse(poseStack, tesselator, NarakaTextures.ECLIPSE);
        }
    }

    @Inject(method = "renderSky", at = @At("RETURN"))
    private void renderDimensionSky(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        if (level == null)
            return;
        DimensionSkyRendererRegistry.get(level.dimension())
                .renderSky(level, frustumMatrix, projectionMatrix, partialTick, camera, isFoggy, skyFogSetup);
    }

    @Unique
    private static boolean naraka$isHerobrineSkyEnabled() {
        return NarakaClientContext.ENABLE_HEROBRINE_SKY.getValue();
    }
}
