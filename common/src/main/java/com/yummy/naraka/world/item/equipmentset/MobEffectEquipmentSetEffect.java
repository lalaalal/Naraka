package com.yummy.naraka.world.item.equipmentset;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class MobEffectEquipmentSetEffect implements EquipmentSetEffect {
    private final Map<Holder<MobEffect>, Integer> effects;

    @SafeVarargs
    public MobEffectEquipmentSetEffect(Holder<MobEffect>... effects) {
        this.effects = new HashMap<>(effects.length);
        for (Holder<MobEffect> effect : effects) {
            this.effects.put(effect, 0);
        }
    }

    public MobEffectEquipmentSetEffect(Map<Holder<MobEffect>, Integer> effects) {
        this.effects = Map.copyOf(effects);
    }

    @Override
    public void activate(LivingEntity livingEntity) {
        effects.forEach((effect, amplifier) -> {
            livingEntity.addEffect(new MobEffectInstance(effect, -1, amplifier));
        });
    }

    @Override
    public void deactivate(LivingEntity livingEntity) {
        effects.keySet().forEach(livingEntity::removeEffect);
    }
}
