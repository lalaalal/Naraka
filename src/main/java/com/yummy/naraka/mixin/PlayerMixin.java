package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yummy.naraka.util.NarakaItemUtils;
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

    @ModifyReturnValue(method = "isPushedByFluid", at = @At("RETURN"))
    public boolean isPushedByFluid(boolean original) {
        return original && super.isPushedByFluid();
    }

    @ModifyExpressionValue(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    public boolean ignoreEyeInWaterWithEfficientMiningInWater(boolean original) {
        if (NarakaItemUtils.canApplyEfficientMiningInWater(this))
            return false;
        return original;
    }

    @ModifyExpressionValue(method = "getDestroySpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onGround()Z"))
    public boolean considerOnGroundWithEfficientMimingInAir(boolean original) {
        if (NarakaItemUtils.canApplyEfficientMiningInAir(this))
            return true;
        return original;
    }
}
