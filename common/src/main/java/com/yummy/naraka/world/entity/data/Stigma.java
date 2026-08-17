package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.config.NarakaCommonConfig;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.StigmatizingEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @param value          Actual value of stigma (0 ~ 3)
 * @param lastMarkedTime
 * @see StigmaHelper
 */
public record Stigma(int value, long lastMarkedTime, Optional<EntityReference<LivingEntity>> cause) {
    public static final Codec<Stigma> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("value").forGetter(Stigma::value),
            Codec.LONG.fieldOf("lastMarkedTime").forGetter(Stigma::lastMarkedTime),
            EntityReference.<LivingEntity>codec().optionalFieldOf("cause").forGetter(Stigma::cause)
            ).apply(instance, Stigma::new)
    );
    public static final StreamCodec<ByteBuf, Stigma> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            Stigma::value,
            ByteBufCodecs.LONG,
            Stigma::lastMarkedTime,
            ByteBufCodecs.optional(EntityReference.streamCodec()),
            Stigma::cause,
            Stigma::new
    );

    public static final Stigma ZERO = new Stigma(0, 0, Optional.empty());
    public static final int MAX_STIGMA = 2;

    public Stigma(int value, long lastMarkedTime, LivingEntity cause) {
        this(value, lastMarkedTime, Optional.of(EntityReference.of(cause)));
    }

    public Stigma(int value, LivingEntity cause) {
        this(value, cause.level().getGameTime(), Optional.of(EntityReference.of(cause)));
    }

    public Optional<LivingEntity> getCause(Level level) {
        return cause.map(reference -> EntityReference.getLivingEntity(reference, level));
    }

    /**
     * Increase value of stigma.
     * Update {@link #lastMarkedTime} if recordTime is true or set {@linkplain #lastMarkedTime} to 0.<br>
     * Consume stigma if current stigma value is bigger than {@link #MAX_STIGMA}.
     *
     * @param livingEntity Target entity to increase entity
     * @param cause        Entity that causes the stigma to be increased
     * @return Updated value of stigma
     * @see Stigma#consume(ServerLevel, LivingEntity, LivingEntity)
     */
    public Stigma increase(ServerLevel level, LivingEntity livingEntity, LivingEntity cause) {
        long time = livingEntity.level().getGameTime();
        if (value < MAX_STIGMA)
            return increased(time, cause);
        return consume(level, livingEntity, cause);
    }

    private Stigma increased(long time, LivingEntity cause) {
        return new Stigma(value + 1, time, cause);
    }

    public Stigma decrease() {
        if (value > 0)
            return new Stigma(value - 1, lastMarkedTime, cause);
        return this;
    }

    /**
     * Reset the stigma of living entity to 0.
     * Stun and lock health of living entity for {@link NarakaCommonConfig#stigmaStunDuration} ticks.<br>
     * Call {@link StigmatizingEntity#collectStigma(ServerLevel, LivingEntity, Stigma)} if caused entity is {@linkplain StigmatizingEntity}
     *
     * @param livingEntity Target entity to consume stigma
     * @param cause        Entity that causes the stigma to be consumed
     * @return Stigma with value 0 and current time
     * @see StunHelper#stunEntity(LivingEntity, int)
     * @see LockedHealthHelper#lock(LivingEntity, double)
     * @see StigmatizingEntity#collectStigma(ServerLevel, LivingEntity, Stigma)
     */
    public Stigma consume(ServerLevel level, LivingEntity livingEntity, LivingEntity cause) {
        int stunDuration = NarakaConfig.COMMON.stigmaStunDuration.getValue();
        level.sendParticles(NarakaParticleTypes.LOCKED_HEALTH.get(), livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ(), 0, 0, 0, 0, 1);
        lockHealth(level, livingEntity, cause);
        StunHelper.stunEntity(livingEntity, stunDuration);
        livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.TOTEM_USE, livingEntity.getSoundSource(), 1.0F, 1.0F);

        if (cause instanceof StigmatizingEntity stigmatizingEntity) {
            stigmatizingEntity.collectStigma(level, livingEntity, this);
        } else {
            cause.heal(6);
        }

        return new Stigma(0, lastMarkedTime, cause);
    }

    private void lockHealth(ServerLevel level, LivingEntity livingEntity, LivingEntity cause) {
        double maxHealth = livingEntity.getAttributeValue(Attributes.MAX_HEALTH);
        double reducingHealth = maxHealth * NarakaConfig.COMMON.lockHealthRatio.getValue();
        if (reducingHealth >= livingEntity.getHealth()) {
            DamageSource source = NarakaDamageSources.stigma(cause);
            livingEntity.hurtServer(level, source, 6.66e6f);
        } else {
            LockedHealthHelper.lock(livingEntity, reducingHealth);
        }
    }
}
