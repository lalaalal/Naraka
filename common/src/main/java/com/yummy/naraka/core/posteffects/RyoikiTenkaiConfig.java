package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record RyoikiTenkaiConfig(DynamicPostEffectType<?> type, float restoreStartTime,
                                 float restoreDuration) implements PostEffectConfig {
    public static final MapCodec<RyoikiTenkaiConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DynamicPostEffectType.CODEC.fieldOf("type").forGetter(RyoikiTenkaiConfig::type),
                    Codec.FLOAT.fieldOf("restore_start_time").forGetter(RyoikiTenkaiConfig::restoreStartTime),
                    Codec.FLOAT.fieldOf("restore_duration").forGetter(RyoikiTenkaiConfig::restoreDuration)
            ).apply(instance, RyoikiTenkaiConfig::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RyoikiTenkaiConfig> STREAM_CODEC = StreamCodec.composite(
            DynamicPostEffectType.STREAM_CODEC,
            RyoikiTenkaiConfig::type,
            ByteBufCodecs.FLOAT,
            RyoikiTenkaiConfig::restoreStartTime,
            ByteBufCodecs.FLOAT,
            RyoikiTenkaiConfig::restoreDuration,
            RyoikiTenkaiConfig::new
    );

    public static RyoikiTenkaiConfig green(float restoreStartTime, float restoreDuration) {
        return new RyoikiTenkaiConfig(NarakaDynamicPostEffectTypes.RYOIKI_GREEN.get(), restoreStartTime, restoreDuration);
    }

    public static RyoikiTenkaiConfig gray(float restoreStartTime, float restoreDuration) {
        return new RyoikiTenkaiConfig(NarakaDynamicPostEffectTypes.RYOIKI_GRAY.get(), restoreStartTime, restoreDuration);
    }
}
