package com.yummy.naraka.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class Herobrine extends Monster {
    public Herobrine(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        registerGoals();
    }

    public static AttributeSupplier getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .build();
    }
}
