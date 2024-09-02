package com.yummy.naraka.mixin;

import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
        if (self() instanceof LivingEntity livingEntity
                && NarakaItemUtils.canApplyReinforcementEffect(livingEntity, EquipmentSlot.LEGS, NarakaReinforcementEffects.IGNORE_LIQUID_PUSH)
        ) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }

    @Unique
    private Entity self() {
        return (Entity) (Object) this;
    }
}
