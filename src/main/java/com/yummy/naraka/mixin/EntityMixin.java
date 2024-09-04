package com.yummy.naraka.mixin;

import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract boolean isInLiquid();

    @Shadow
    public abstract void setSwimming(boolean swimming);

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract boolean isPassenger();

    @Inject(method = "updateSwimming", at = @At("HEAD"), cancellable = true)
    public void updateSwimming(CallbackInfo ci) {
        if (self() instanceof LivingEntity livingEntity
                && NarakaItemUtils.canApplyReinforcementEffect(livingEntity, EquipmentSlot.LEGS, NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING)) {
            this.setSwimming(this.isSprinting() && this.isInLiquid() && !this.isPassenger());
            ci.cancel();
        }
    }

    @Unique
    private Entity self() {
        return (Entity) (Object) this;
    }
}
