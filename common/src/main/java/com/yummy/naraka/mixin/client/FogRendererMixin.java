package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import com.yummy.naraka.util.NarakaItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Shadow
    private static boolean fogEnabled;

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void visibleWithLavaVision(Camera camera, FogRenderer.FogMode fogMode, Vector4f fogColor, float renderDistance, boolean isFoggy, float partialTick, CallbackInfoReturnable<FogParameters> cir) {
        if (!fogEnabled)
            return;
        FogType fogType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();
        FogRenderer.FogData fogData = new FogRenderer.FogData(fogMode);
        if (WhiteFogRenderHelper.shouldApplyWhiteFog()) {
            cir.cancel();
            cir.setReturnValue(WhiteFogRenderHelper.getWhiteFogParameters(fogColor, renderDistance, partialTick));
            return;
        }

        if (fogType == FogType.LAVA
                && entity instanceof LivingEntity livingEntity
                && NarakaItemUtils.canApplyLavaVision(livingEntity)) {
            fogData.start = -8;
            fogData.end = 96;
            fogData.shape = FogShape.SPHERE;
            cir.cancel();
            cir.setReturnValue(new FogParameters(fogData.start, fogData.end, fogData.shape, fogColor.x, fogColor.y, fogColor.z, fogColor.w));
        }
    }

    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private static void computeFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, CallbackInfoReturnable<Vector4f> cir) {
        Vector4f color = cir.getReturnValue();
        if (WhiteFogRenderHelper.shouldApplyWhiteFog()) {
            cir.setReturnValue(WhiteFogRenderHelper.getFogColor(color, partialTick));
            cir.cancel();
        }
    }
}
