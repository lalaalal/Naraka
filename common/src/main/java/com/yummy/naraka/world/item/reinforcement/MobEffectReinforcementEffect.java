package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MobEffectReinforcementEffect extends SimpleReinforcementEffect {
    protected final Holder<MobEffect> mobEffect;

    public MobEffectReinforcementEffect(Holder<MobEffect> mobEffect, int requiredReinforcement, EquipmentSlot... slots) {
        super(requiredReinforcement, slots);
        this.mobEffect = mobEffect;
    }

    @Override
    public void onEquipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        entity.addEffect(new MobEffectInstance(mobEffect, -1));
    }

    @Override
    public void onUnequipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        entity.removeEffect(mobEffect);
    }
}
