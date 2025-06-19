package com.yummy.naraka.world.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class DiamondGolem extends Monster {
    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.STEP_HEIGHT, 0.75)
                .add(Attributes.MOVEMENT_SPEED, 0.17f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.SAFE_FALL_DISTANCE, 256)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0)
                .add(Attributes.MAX_HEALTH, 300);
    }

    public DiamondGolem(EntityType<? extends DiamondGolem> entityType, Level level) {
        super(entityType, level);
        setPersistenceRequired();
    }

    @Override
    public int getAirSupply() {
        return getMaxAirSupply();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource damageSource) {
        if (damageSource.is(DamageTypes.IN_WALL))
            return true;
        return super.isInvulnerableTo(level, damageSource);
    }
}
