package com.yummy.naraka.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffectHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(method = "travelInFluid(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/material/FluidState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    public float increaseSpeedInLiquid(float scale) {
        return ReinforcementEffectHelper.increaseSpeedInLiquid(naraka$living(), scale);
    }

    @ModifyExpressionValue(method = "travelInFluid(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/material/FluidState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    public boolean considerLiquidAsWater(boolean original) {
        return ReinforcementEffectHelper.considerLiquidAsWater(naraka$living(), original);
    }

    @Unique
    protected LivingEntity naraka$living() {
        return (LivingEntity) (Object) this;
    }
}
