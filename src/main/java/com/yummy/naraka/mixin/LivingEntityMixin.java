package com.yummy.naraka.mixin;

import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract double getAttributeValue(Holder<Attribute> attribute);

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

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInLava()Z", shift = At.Shift.AFTER))
    protected void travel(Vec3 travelVector, CallbackInfo ci) {
        if (isInLava()) {
            double waterMovementEfficiency = getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY) * 2;
            setDeltaMovement(getDeltaMovement().multiply(waterMovementEfficiency, 1, waterMovementEfficiency));
        }
    }

    @Unique
    private LivingEntity self() {
        return (LivingEntity) (Object) this;
    }
}
