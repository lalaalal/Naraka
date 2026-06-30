package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @ModifyExpressionValue(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;getExplosionKnockbackAfterDampener(Lnet/minecraft/world/entity/LivingEntity;D)D"))
    public double preventKnockback(double original, @Local LivingEntity livingEntity) {
        if (livingEntity.getType().is(NarakaEntityTypeTags.HEROBRINE))
            return 0;
        return original;
    }
}
