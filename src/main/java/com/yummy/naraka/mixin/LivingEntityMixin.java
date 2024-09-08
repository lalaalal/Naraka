package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void saveEntityData(CompoundTag compoundTag, CallbackInfo ci) {
        if (EntityDataHelper.hasEntityData(living()))
            EntityDataHelper.saveEntityData(living(), compoundTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void readEntityData(CompoundTag compoundTag, CallbackInfo ci) {
        EntityDataHelper.readEntityData(living(), compoundTag);
    }

    @Inject(method = "remove", at = @At("RETURN"))
    public void removeEntityData(RemovalReason removalReason, CallbackInfo ci) {
        if (removalReason.shouldDestroy())
            EntityDataHelper.removeEntityData(living());
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    public float increaseSpeedInLiquid(float scale) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(living())) {
            if (isInLava() && isSwimming())
                return scale * 15;
            return scale * 5;
        }
        return scale;
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    public boolean considerLiquidAsWater(boolean original) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(living()))
            return isInLiquid();
        return original;
    }

    @Unique
    protected boolean isUnderLiquid() {
        FluidState fluidState = level().getFluidState(BlockPos.containing(getEyePosition()));
        return !fluidState.isEmpty() && isInLiquid();
    }

    @Override
    public void updateSwimming() {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(living())) {
            if (isSwimming()) {
                this.setSwimming(this.isSprinting() && this.isInLiquid() && !this.isPassenger());
            } else {
                this.setSwimming(this.isSprinting() && this.isUnderLiquid() && !this.isPassenger() && !level().getFluidState(blockPosition()).isEmpty());
            }
        }
    }

    @Override
    public boolean isPushedByFluid() {
        if (NarakaItemUtils.canApplyIgnoreLiquidPushing(living()))
            return false;
        return super.isPushedByFluid();
    }

    @Unique
    protected LivingEntity living() {
        return (LivingEntity) (Object) this;
    }
}
