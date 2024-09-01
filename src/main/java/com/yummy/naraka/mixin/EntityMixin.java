package com.yummy.naraka.mixin;

import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "updateFluidHeightAndDoFluidPushing", at = @At("HEAD"), cancellable = true)
    protected void updateInWaterStateAndDoWaterCurrentPushing(TagKey<Fluid> fluidTag, double motionScale, CallbackInfoReturnable<Boolean> cir) {
        if (self() instanceof LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
            Reinforcement reinforcement = itemStack.getOrDefault(NarakaDataComponentTypes.REINFORCEMENT, Reinforcement.ZERO);
            if (reinforcement.canApplyEffect(NarakaReinforcementEffects.IGNORE_LIQUID_PUSH, livingEntity, EquipmentSlot.LEGS, itemStack)) {
                cir.cancel();
                cir.setReturnValue(false);
            }
        }
    }

    @Unique
    private Entity self() {
        return (Entity) (Object) this;
    }
}
