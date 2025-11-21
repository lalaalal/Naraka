package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.entity.ScarfWavingData;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import com.yummy.naraka.world.item.equipmentset.NarakaEquipmentSets;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
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
    @Shadow @Final
    private static EntityDataAccessor<Float> DATA_HEALTH_ID;

    @Shadow @Final
    private Map<EquipmentSlot, ItemStack> lastEquipmentItems;

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyArg(
            method = {"travelInFluid(Lnet/minecraft/world/phys/Vec3;)V", "travelInFluid(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/material/FluidState;)V"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V")
    )
    public float increaseSpeedInLiquid(float scale) {
        return ReinforcementEffectHelper.increaseSpeedInLiquid(naraka$living(), scale);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyExpressionValue(
            method = {"travelInFluid(Lnet/minecraft/world/phys/Vec3;)V", "travelInFluid(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/material/FluidState;)V"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z")
    )
    public boolean considerLiquidAsWater(boolean original) {
        return ReinforcementEffectHelper.considerLiquidAsWater(naraka$living(), original);
    }

    /**
     * Using {@linkplain Float#MAX_VALUE} for damage may occur setting health as Nan!
     */
    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    public void fixNanHealth(float health, CallbackInfo ci) {
        if (Float.isNaN(health))
            health = 0;
        this.entityData.set(DATA_HEALTH_ID, Mth.clamp(health, 0.0F, this.getMaxHealth()));
        ci.cancel();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void checkScarfEquipment(CallbackInfo ci) {
        if (!EntityDataHelper.hasEntityData(naraka$living(), NarakaEntityDataTypes.SCARF_WAVING_DATA.get())) {
            ItemStack itemStack = getItemBySlot(EquipmentSlot.CHEST);
            if (itemStack.has(NarakaDataComponentTypes.HEROBRINE_SCARF.get()))
                EntityDataHelper.setEntityData(naraka$living(), NarakaEntityDataTypes.SCARF_WAVING_DATA.get(), new ScarfWavingData());
        }
    }

    @Unique
    protected boolean naraka$isUnderLiquid() {
        FluidState fluidState = level().getFluidState(BlockPos.containing(getEyePosition()));
        return !fluidState.isEmpty() && isInLiquid();
    }

    @Override
    public boolean isInWater() {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(naraka$living()))
            return this.wasTouchingWater || isInLava();
        return this.wasTouchingWater;
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
        return lastEquipmentItems.getOrDefault(slot, ItemStack.EMPTY);
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
