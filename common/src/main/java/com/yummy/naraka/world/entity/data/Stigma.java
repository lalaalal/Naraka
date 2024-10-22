package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

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
        DamageSource source = NarakaDamageSources.stigma(cause);
        livingEntity.hurt(source, Float.MAX_VALUE);
        NarakaAttributeModifiers.stunEntity(livingEntity, HOLD_ENTITY_DURATION);

        return new Stigma(0, now);
    }

    public Stigma tryDecrease(LivingEntity livingEntity) {
        long now = livingEntity.level().getGameTime();
        if (now - lastMarkedTime >= STIGMA_DECREASE_COOLDOWN)
            return decrease(now);
        return this;
    }
}
