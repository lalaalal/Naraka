package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yummy.naraka.util.NarakaItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void visibleWithLavaVision(Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance, boolean shouldCreateFog, float partialTick, CallbackInfo ci) {
        FogType fogType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();
        if (fogType == FogType.LAVA
                && entity instanceof LivingEntity livingEntity
                && NarakaItemUtils.canApplyLavaVision(livingEntity)) {
            ci.cancel();
            RenderSystem.setShaderFogStart(-8);
            RenderSystem.setShaderFogEnd(96);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        }
    }
}
