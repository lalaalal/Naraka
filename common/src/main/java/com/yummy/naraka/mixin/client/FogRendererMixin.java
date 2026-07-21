package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @ModifyExpressionValue(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSpectator()Z"))
    private static boolean setupForLavaVision(boolean original, @Local Entity entity) {
        if (entity instanceof LivingEntity livingEntity && NarakaItemUtils.canApplyLavaVision(livingEntity))
            return true;
        return original;
    }
}
