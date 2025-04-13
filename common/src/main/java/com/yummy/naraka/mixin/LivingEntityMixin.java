package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.item.equipmentset.NarakaEquipmentSets;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    protected abstract ItemStack getLastHandItem(EquipmentSlot slot);

    @Shadow
    protected abstract ItemStack getLastArmorItem(EquipmentSlot slot);

    @Shadow private float speed;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void saveEntityData(CompoundTag compoundTag, CallbackInfo ci) {
        if (EntityDataHelper.hasEntityData(naraka$living()))
            EntityDataHelper.saveEntityData(naraka$living(), compoundTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void readEntityData(CompoundTag compoundTag, CallbackInfo ci) {
        EntityDataHelper.readEntityData(naraka$living(), compoundTag);
    }

    @Inject(method = "remove", at = @At("RETURN"))
    public void removeEntityData(RemovalReason removalReason, CallbackInfo ci) {
        if (removalReason.shouldDestroy())
            EntityDataHelper.removeEntityData(naraka$living());
    }

    /**
     * Using {@linkplain Float#MAX_VALUE} for damage may occur setting health as Nan!
     */
    @ModifyArg(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    public float fixNanHealth(float original) {
        if (Float.isNaN(original))
            return 0;
        return original;
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    public float increaseSpeedInLiquid(float scale) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(naraka$living())) {
            float speedModifier = NarakaConfig.COMMON.fasterLiquidSwimmingSpeed.getValue();
            if (isInLava() && isSwimming())
                return scale * speedModifier * 3;
            return scale * speedModifier;
        }
        return scale;
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    public boolean considerLiquidAsWater(boolean original) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(naraka$living()))
            return isInLiquid();
        return original;
    }

    @Unique
    protected boolean naraka$isUnderLiquid() {
        FluidState fluidState = level().getFluidState(BlockPos.containing(getEyePosition()));
        return !fluidState.isEmpty() && isInLiquid();
    }

    @Override
    public void updateSwimming() {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(naraka$living())) {
            if (isSwimming()) {
                this.setSwimming(this.isSprinting() && this.isInLiquid() && !this.isPassenger());
            } else {
                this.setSwimming(this.isSprinting() && this.naraka$isUnderLiquid() && !this.isPassenger() && !level().getFluidState(blockPosition()).isEmpty());
            }
        } else {
            super.updateSwimming();
        }
    }

    @Override
    public boolean isPushedByFluid() {
        if (NarakaItemUtils.canApplyIgnoreLiquidPushing(naraka$living()))
            return false;
        return super.isPushedByFluid();
    }

    @Inject(method = "decreaseAirSupply", at = @At("HEAD"), cancellable = true)
    protected void preserveAirSupply(int currentAir, CallbackInfoReturnable<Integer> cir) {
        if (NarakaItemUtils.canApplyWaterBreathing(naraka$living())) {
            cir.cancel();
            cir.setReturnValue(currentAir);
        }
    }

    @Inject(method = "handleEquipmentChanges", at = @At(value = "HEAD"))
    private void handleEquipmentChanges(Map<EquipmentSlot, ItemStack> equipments, CallbackInfo ci) {
        for (EquipmentSlot slot : equipments.keySet()) {
            ItemStack currentStack = equipments.get(slot);
            ItemStack previousStack = naraka$getPreviousStack(slot);

            naraka$handleEquipmentSetEffect(naraka$living(), slot, previousStack, currentStack);
            naraka$handleReinforcementEffect(naraka$living(), slot, previousStack, currentStack);
        }
    }

    @Unique
    private ItemStack naraka$getPreviousStack(EquipmentSlot slot) {
        EquipmentSlot.Type type = slot.getType();
        if (type == EquipmentSlot.Type.HAND)
            return getLastHandItem(slot);
        if (type == EquipmentSlot.Type.HUMANOID_ARMOR)
            return getLastArmorItem(slot);
        return ItemStack.EMPTY;
    }

    @Unique
    private static void naraka$handleReinforcementEffect(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        if (Reinforcement.get(previousStack) == Reinforcement.get(currentStack)) {
            NarakaItemUtils.checkAndUpdateReinforcementEffects(livingEntity, equipmentSlot, currentStack,
                    ReinforcementEffect::onEquippedItemChanged);
            return;
        }

        NarakaItemUtils.updateReinforcementEffects(livingEntity, equipmentSlot, previousStack,
                ReinforcementEffect::onUnequipped);
        NarakaItemUtils.checkAndUpdateReinforcementEffects(livingEntity, equipmentSlot, currentStack,
                ReinforcementEffect::onEquipped);
    }

    @Unique
    private static void naraka$handleEquipmentSetEffect(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        NarakaEquipmentSets.updateAllSetEffects(livingEntity);
    }

    @Unique
    protected LivingEntity naraka$living() {
        return (LivingEntity) (Object) this;
    }
}
