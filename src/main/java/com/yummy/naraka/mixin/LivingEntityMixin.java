package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (EntityDataHelper.hasEntityData(self()))
            EntityDataHelper.saveEntityData(self(), compoundTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        EntityDataHelper.readEntityData(self(), compoundTag);
    }

    @Inject(method = "remove", at = @At("RETURN"))
    public void remove(Entity.RemovalReason removalReason, CallbackInfo ci) {
        if (removalReason.shouldDestroy())
            EntityDataHelper.removeEntityData(self());
    }

    @ModifyReturnValue(method = "getWaterSlowDown", at = @At("RETURN"))
    protected float getWaterSlowDown(float original) {
        if (NarakaItemUtils.canApplyReinforcementEffect(
                self(), EquipmentSlot.CHEST, NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING
        )) return -original;
        return original;
    }

    @Unique
    private LivingEntity self() {
        return (LivingEntity) (Object) this;
    }
}
