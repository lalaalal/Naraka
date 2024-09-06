package com.yummy.naraka.mixin;

import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
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
        if (EntityDataHelper.hasEntityData(self()))
            EntityDataHelper.saveEntityData(self(), compoundTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void readEntityData(CompoundTag compoundTag, CallbackInfo ci) {
        EntityDataHelper.readEntityData(self(), compoundTag);
    }

    @Inject(method = "remove", at = @At("RETURN"))
    public void removeEntityData(RemovalReason removalReason, CallbackInfo ci) {
        if (removalReason.shouldDestroy())
            EntityDataHelper.removeEntityData(self());
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    public float increaseSpeedInLiquid(float scale) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(self()))
            return scale * 5;
        return scale;
    }

    @Unique
    private LivingEntity self() {
        return (LivingEntity) (Object) this;
    }
}
