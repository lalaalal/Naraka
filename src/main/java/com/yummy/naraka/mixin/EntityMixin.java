package com.yummy.naraka.mixin;

import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Unique
    private static final ReinforcementEffect IGNORE_WATER_PUSH_INSTANCE = NarakaReinforcementEffects.IGNORE_WATER_PUSH.value();

    @Inject(method = "updateInWaterStateAndDoWaterCurrentPushing", at = @At("HEAD"), cancellable = true)
    protected void updateInWaterStateAndDoWaterCurrentPushing(CallbackInfo ci) {
        if (self() instanceof LivingEntity livingEntity) {
            for (ItemStack itemStack : livingEntity.getArmorSlots()) {
                EquipmentSlot slot = livingEntity.getEquipmentSlotForItem(itemStack);
                Reinforcement reinforcement = itemStack.getOrDefault(NarakaDataComponentTypes.REINFORCEMENT, Reinforcement.ZERO);
                if (reinforcement.effects().contains(NarakaReinforcementEffects.IGNORE_WATER_PUSH)
                        && IGNORE_WATER_PUSH_INSTANCE.canApply(livingEntity, slot, itemStack, reinforcement.value()))
                    if (livingEntity.getFluidHeight(FluidTags.WATER) > 0)
                        livingEntity.updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 0.014);
                    ci.cancel();
            }
        }
    }

    @Unique
    private Entity self() {
        return (Entity) (Object) this;
    }
}
