package com.yummy.naraka.mixin;

import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    private Level level;

    @Shadow
    private BlockPos blockPosition;

    @Shadow
    @Final
    private Set<TagKey<Fluid>> fluidOnEyes;

    @Shadow
    public abstract void setSwimming(boolean swimming);

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract boolean isPassenger();

    @Shadow
    public abstract boolean isSwimming();

    @Shadow
    public abstract boolean isInLiquid();

    @Unique
    protected boolean isUnderLiquid() {
        return !fluidOnEyes.isEmpty() && isInLiquid();
    }

    @Inject(method = "updateSwimming", at = @At("HEAD"), cancellable = true)
    public void updateSwimming(CallbackInfo ci) {
        if (self() instanceof LivingEntity livingEntity
                && NarakaItemUtils.canApplyReinforcementEffect(livingEntity, EquipmentSlot.LEGS, NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING)) {
            if (isSwimming()) {
                this.setSwimming(this.isSprinting() && this.isInLiquid() && !this.isPassenger());
            } else {
                this.setSwimming(this.isSprinting() && this.isUnderLiquid() && !this.isPassenger() && !level.getFluidState(blockPosition).isEmpty());
            }
            ci.cancel();
        }
    }

    @Unique
    private Entity self() {
        return (Entity) (Object) this;
    }
}
