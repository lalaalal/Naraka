package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.StunHelper;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public record Stigma(int value, long lastMarkedTime) {
    public static final Stigma ZERO = new Stigma(0, 0);
    public static final int MAX_STIGMA = 2;
    public static final int HOLD_ENTITY_DURATION = 20 * 5;
    public static final int STIGMA_DECREASE_COOLDOWN = 20 * 60;

    public Stigma increase(LivingEntity livingEntity, Entity cause) {
        long now = livingEntity.level().getGameTime();
        if (value < MAX_STIGMA)
            return new Stigma(value + 1, now);
        return consume(livingEntity, cause);
    }

    public Stigma decrease(long now) {
        if (value > 0)
            return new Stigma(value - 1, now);
        return this;
    }

    public Stigma consume(LivingEntity livingEntity, Entity cause) {
        long now = livingEntity.level().getGameTime();
        if (livingEntity != cause && cause instanceof LivingEntity livingCause)
            livingCause.heal(66);
        reduceHealth(livingEntity, cause);
        StunHelper.stunEntity(livingEntity, HOLD_ENTITY_DURATION);
        livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.TOTEM_USE, livingEntity.getSoundSource(), 1.0F, 1.0F);

        return new Stigma(0, now);
    }

    private void reduceHealth(LivingEntity livingEntity, Entity cause) {
        float maxHealth = livingEntity.getMaxHealth();
        double lockedHealth = EntityDataHelper.getEntityData(livingEntity, NarakaEntityDataTypes.LOCKED_HEALTH.get());
        double originalMaxHealth = maxHealth + lockedHealth;
        double reducingHealth = originalMaxHealth * 0.2;
        lockedHealth += reducingHealth;

        if (lockedHealth >= originalMaxHealth) {
            DamageSource source = NarakaDamageSources.stigma(cause);
            livingEntity.hurt(source, Float.MAX_VALUE);
        } else {
            AttributeModifier maxHealthModifier = NarakaAttributeModifiers.reduceMaxHealth(lockedHealth);
            NarakaAttributeModifiers.addPermanentModifier(livingEntity, Attributes.MAX_HEALTH, maxHealthModifier);
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.LOCKED_HEALTH.get(), lockedHealth);
        }
    }

    public Stigma tryDecrease(LivingEntity livingEntity) {
        long now = livingEntity.level().getGameTime();
        if (now - lastMarkedTime >= STIGMA_DECREASE_COOLDOWN)
            return decrease(now);
        return this;
    }
}
