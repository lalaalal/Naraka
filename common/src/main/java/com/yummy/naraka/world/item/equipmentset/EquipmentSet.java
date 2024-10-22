package com.yummy.naraka.world.item.equipmentset;

import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class EquipmentSet {
    private final Predicate<LivingEntity> equipmentTest;
    private final EquipmentSetEffect effect;

    public EquipmentSet(Predicate<LivingEntity> equipmentTest, EquipmentSetEffect effect) {
        this.equipmentTest = equipmentTest;
        this.effect = effect;
    }

    public void updateEffect(LivingEntity entity) {
        if (equipmentTest.test(entity))
            effect.activate(entity);
        else effect.deactivate(entity);
    }
}
