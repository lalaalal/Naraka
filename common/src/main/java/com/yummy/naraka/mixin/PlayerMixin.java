package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.data.StunHelper;
import net.minecraft.world.InteractionHand;
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

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyExpressionValue(
            method = {"getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;getValue()D")
    )
    public double ignoreEyeInWaterWithEfficientMiningInWater(double original) {
        if (NarakaEntityUtils.canApplyEfficientMiningInWater(this))
            return 1;
        return original;
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyExpressionValue(
            method = {"getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onGround()Z")
    )
    public boolean considerOnGroundWithEfficientMimingInAir(boolean original) {
        if (NarakaEntityUtils.canApplyEfficientMiningInAir(this))
            return false;
        return original;
    }

    @Override
    public void startUsingItem(InteractionHand hand) {
        if (StunHelper.isStun(this))
            return;
        super.startUsingItem(hand);
    }
}
