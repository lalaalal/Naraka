package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.util.NarakaItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.renderer.fog.environment.LavaFogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LavaFogEnvironment.class)
public abstract class LavaFogEnvironmentMixin extends FogEnvironment {
    @ModifyExpressionValue(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSpectator()Z"))
    public boolean setupFog(boolean original, @Local(argsOnly = true) Entity entity) {
        if (entity instanceof LivingEntity livingEntity && NarakaItemUtils.canApplyLavaVision(livingEntity))
            return true;
        return original;
    }
}
