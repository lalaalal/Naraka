package com.yummy.naraka.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffectHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    public boolean ignoreEyeInWaterWithEfficientMiningInWater(boolean original) {
        return ReinforcementEffectHelper.ignoreEyeInWaterWithEfficientMiningInWater(this, original);
    }

    @ModifyExpressionValue(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onGround()Z"))
    public boolean considerOnGroundWithEfficientMimingInAir(boolean original) {
        return ReinforcementEffectHelper.considerOnGroundWithEfficientMimingInAir(this, original);
    }
}
