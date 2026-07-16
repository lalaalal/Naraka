package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public record MobEffectData(MobEffect effect, int duration, int amplifier) {
    public static final Codec<MobEffectData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectData::effect),
                    Codec.INT.fieldOf("duration").forGetter(MobEffectData::duration),
                    Codec.INT.fieldOf("amplifier").forGetter(MobEffectData::amplifier)
            ).apply(instance, MobEffectData::new)
    );
}
