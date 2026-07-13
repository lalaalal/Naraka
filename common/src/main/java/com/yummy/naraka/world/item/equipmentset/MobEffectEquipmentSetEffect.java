package com.yummy.naraka.world.item.equipmentset;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class MobEffectEquipmentSetEffect extends EquipmentSetEffectType<List<MobEffectData>> {
    public MobEffectEquipmentSetEffect() {
        super(MobEffectData.CODEC.listOf());
    }

    @Override
    public void activate(LivingEntity livingEntity, List<MobEffectData> data) {
        for (MobEffectData mobEffectData : data)
            livingEntity.addEffect(new MobEffectInstance(mobEffectData.effect(), mobEffectData.duration(), mobEffectData.amplifier()));
    }

    @Override
    public void deactivate(LivingEntity livingEntity, List<MobEffectData> data) {
        for (MobEffectData mobEffectData : data)
            livingEntity.removeEffect(mobEffectData.effect());
    }

}
