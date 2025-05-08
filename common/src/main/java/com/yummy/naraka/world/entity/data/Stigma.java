package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.StigmatizingEntity;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * @param value          Actual value of stigma (0 ~ 3)
 * @param lastMarkedTime
 * @see StigmaHelper
 */
public record Stigma(int value, long lastMarkedTime) {
    public static final Stigma ZERO = new Stigma(0, 0);
    public static final int MAX_STIGMA = 2;
    public static final int HOLD_ENTITY_DURATION = 20 * 5;

    /**
     * Increase value of stigma.
     * Update {@link #lastMarkedTime} if recordTime is true or set {@linkplain #lastMarkedTime} to 0.<br>
     * Consume stigma if current stigma value is bigger than {@link #MAX_STIGMA}.
     *
     * @param livingEntity Target entity to increase entity
     * @param cause        Entity that causes the stigma to be increased
     * @param recordTime   Update {@link #lastMarkedTime} if value is true
     * @return Updated value of stigma
     * @see Stigma#consume(ServerLevel, LivingEntity, Entity)
     */
    public Stigma increase(ServerLevel level, LivingEntity livingEntity, Entity cause, boolean recordTime) {
        long time = recordTime ? livingEntity.level().getGameTime() : lastMarkedTime;
        if (value < MAX_STIGMA)
            return increased(time);
        return increased(time).consume(level, livingEntity, cause);
    }

    private Stigma increased(long time) {
        return new Stigma(value + 1, time);
    }

    public Stigma decrease(long time) {
        if (value > 0)
            return new Stigma(value - 1, time);
        return this;
    }

    /**
     * Reset the stigma of living entity to 0.
     * Stun and lock health of living entity for {@link #HOLD_ENTITY_DURATION} ticks.<br>
     * Call {@link StigmatizingEntity#collectStigma(ServerLevel, LivingEntity, Stigma)} if caused entity is {@linkplain StigmatizingEntity}
     *
     * @param livingEntity Target entity to consume stigma
     * @param cause        Entity that causes the stigma to be consumed
     * @return Stigma with value 0 and current time
     * @see StunHelper#stunEntity(LivingEntity, int)
     * @see LockedHealthHelper#lock(LivingEntity, double)
     * @see StigmatizingEntity#collectStigma(ServerLevel, LivingEntity, Stigma)
     */
    public Stigma consume(ServerLevel level, LivingEntity livingEntity, Entity cause) {
        lockHealth(level, livingEntity, cause);
        StunHelper.stunEntity(livingEntity, HOLD_ENTITY_DURATION);
        livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.TOTEM_USE, livingEntity.getSoundSource(), 1.0F, 1.0F);

        if (livingEntity != cause && cause instanceof StigmatizingEntity stigmatizingEntity)
            stigmatizingEntity.collectStigma(level, livingEntity, this);

        return new Stigma(0, lastMarkedTime);
    }

    private void lockHealth(ServerLevel level, LivingEntity livingEntity, Entity cause) {
        float maxHealth = livingEntity.getMaxHealth();
        double lockedHealth = EntityDataHelper.getEntityData(livingEntity, NarakaEntityDataTypes.LOCKED_HEALTH.get());
        double originalMaxHealth = maxHealth + lockedHealth;
        double reducingHealth = originalMaxHealth * 0.2;
        lockedHealth += reducingHealth;

        if (lockedHealth >= originalMaxHealth) {
            DamageSource source = NarakaDamageSources.stigma(cause);
            livingEntity.hurtServer(level, source, Float.MAX_VALUE);
        } else {
            LockedHealthHelper.lock(livingEntity, lockedHealth);
        }
    }
}
