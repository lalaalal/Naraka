package com.yummy.naraka.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.fabric.client.FabricDimensionSpecialEffectsRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(DimensionSpecialEffects.class)
public abstract class DimensionSpecialEffectsMixin {
    @ModifyReturnValue(method = "forType", at = @At(value = "RETURN"))
    private static DimensionSpecialEffects findRegistered(DimensionSpecialEffects original, @Local(argsOnly = true) DimensionType dimensionType) {
        if (FabricDimensionSpecialEffectsRegistry.hasCustomDimensionEffects(dimensionType.effectsLocation()))
            return FabricDimensionSpecialEffectsRegistry.getCustomDimensionEffects(dimensionType.effectsLocation());
        return original;
    }
}
