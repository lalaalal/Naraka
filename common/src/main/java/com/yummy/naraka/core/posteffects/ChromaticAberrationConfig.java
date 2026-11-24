package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ChromaticAberrationConfig(float duration, float intensity, float distortion,
                                        float zoom) implements PostEffectConfig {
    public static final MapCodec<ChromaticAberrationConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("duration").forGetter(ChromaticAberrationConfig::duration),
                    Codec.FLOAT.fieldOf("intensity").forGetter(ChromaticAberrationConfig::intensity),
                    Codec.FLOAT.fieldOf("distortion").forGetter(ChromaticAberrationConfig::distortion),
                    Codec.FLOAT.fieldOf("zoom").forGetter(ChromaticAberrationConfig::zoom)
            ).apply(instance, ChromaticAberrationConfig::new)
    );

    public static final StreamCodec<ByteBuf, ChromaticAberrationConfig> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ChromaticAberrationConfig::duration,
            ByteBufCodecs.FLOAT,
            ChromaticAberrationConfig::intensity,
            ByteBufCodecs.FLOAT,
            ChromaticAberrationConfig::distortion,
            ByteBufCodecs.FLOAT,
            ChromaticAberrationConfig::zoom,
            ChromaticAberrationConfig::new
    );

    public static final ChromaticAberrationConfig ZERO = new ChromaticAberrationConfig(0, 0, 0, 0);
    public static final ChromaticAberrationConfig DEFAULT = new ChromaticAberrationConfig(50, 2.5f, 2, 0.75f);
    public static final ChromaticAberrationConfig STRONG = new ChromaticAberrationConfig(75, 2.5f, 2, 0.75f);
    public static final ChromaticAberrationConfig DISTORTION_ONLY = new ChromaticAberrationConfig(50, 0, 3, 0.9f);

    @Override
    public DynamicPostEffectType<?> type() {
        return NarakaDynamicPostEffectTypes.CHROMATIC_ABERRATION.value();
    }
}
