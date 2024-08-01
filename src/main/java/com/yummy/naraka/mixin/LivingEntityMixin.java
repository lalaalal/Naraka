package com.yummy.naraka.mixin;

import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
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

    @Inject(method = "die", at = @At("RETURN"))
    public void die(DamageSource damageSource, CallbackInfo ci) {
        EntityDataHelper.removeEntityData(self());
    }

    @Unique
    private LivingEntity self() {
        return (LivingEntity) (Object) this;
    }
}
