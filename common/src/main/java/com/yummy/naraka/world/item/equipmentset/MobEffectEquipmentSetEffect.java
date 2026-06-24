package com.yummy.naraka.world.item.equipmentset;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class MobEffectEquipmentSetEffect implements EquipmentSetEffect {
    private final MobEffect effect;
    private final boolean visible;

    public MobEffectEquipmentSetEffect(MobEffect effect) {
        this(effect, true);
    }

    public MobEffectEquipmentSetEffect(MobEffect effect, boolean visible) {
        this.effect = effect;
        this.visible = visible;
    }

    @Override
    public void activate(LivingEntity livingEntity) {
        livingEntity.addEffect(new MobEffectInstance(effect, -1, 0, false, visible));
    }

    @Override
    public void deactivate(LivingEntity livingEntity) {
        livingEntity.removeEffect(effect);
    }
}
