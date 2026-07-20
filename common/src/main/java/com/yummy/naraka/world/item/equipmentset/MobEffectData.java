package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;

public record MobEffectData(Holder<MobEffect> effect, int duration, int amplifier) {
    public static final Codec<MobEffectData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    MobEffect.CODEC.fieldOf("effect").forGetter(MobEffectData::effect),
                    Codec.INT.fieldOf("duration").forGetter(MobEffectData::duration),
                    Codec.INT.fieldOf("amplifier").forGetter(MobEffectData::amplifier)
            ).apply(instance, MobEffectData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MobEffectData> STREAM_CODEC = StreamCodec.composite(
            MobEffect.STREAM_CODEC,
            MobEffectData::effect,
            ByteBufCodecs.INT,
            MobEffectData::duration,
            ByteBufCodecs.INT,
            MobEffectData::amplifier,
            MobEffectData::new
    );

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MobEffectData(Holder<MobEffect> otherEffect, int otherDuration, int otherAmplifier)))
            return false;

        return duration == otherDuration && amplifier == otherAmplifier && effect.value().equals(otherEffect.value());
    }

    @Override
    public int hashCode() {
        int result = effect.hashCode();
        result = 31 * result + duration;
        result = 31 * result + amplifier;
        return result;
    }
}
