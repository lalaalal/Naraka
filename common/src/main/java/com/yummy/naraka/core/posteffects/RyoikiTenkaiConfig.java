package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public record RyoikiTenkaiConfig(Type ryoikiType, float restoreStartTime,
                                 float restoreDuration) implements PostEffectConfig {
    public static final MapCodec<RyoikiTenkaiConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Type.CODEC.fieldOf("ryoiki_type").forGetter(RyoikiTenkaiConfig::ryoikiType),
                    Codec.FLOAT.fieldOf("restore_start_time").forGetter(RyoikiTenkaiConfig::restoreStartTime),
                    Codec.FLOAT.fieldOf("restore_duration").forGetter(RyoikiTenkaiConfig::restoreDuration)
            ).apply(instance, RyoikiTenkaiConfig::new)
    );
    public static final StreamCodec<ByteBuf, RyoikiTenkaiConfig> STREAM_CODEC = StreamCodec.composite(
            Type.STREAM_CODEC,
            RyoikiTenkaiConfig::ryoikiType,
            ByteBufCodecs.FLOAT,
            RyoikiTenkaiConfig::restoreStartTime,
            ByteBufCodecs.FLOAT,
            RyoikiTenkaiConfig::restoreDuration,
            RyoikiTenkaiConfig::new
    );

    public static RyoikiTenkaiConfig green(float restoreStartTime, float restoreDuration) {
        return new RyoikiTenkaiConfig(Type.GREEN, restoreStartTime, restoreDuration);
    }

    public static RyoikiTenkaiConfig gray(float restoreStartTime, float restoreDuration) {
        return new RyoikiTenkaiConfig(Type.GRAY, restoreStartTime, restoreDuration);
    }

    @Override
    public PostEffectType<RyoikiTenkaiConfig> type() {
        return ryoikiType.get();
    }

    @SuppressWarnings({"FunctionalExpressionCanBeFolded", "Convert2MethodRef"})
    public enum Type implements StringRepresentable {
        GREEN(() -> NarakaPostEffectTypes.RYOIKI_GREEN.get()),
        GRAY(() -> NarakaPostEffectTypes.RYOIKI_GRAY.get());

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        private final Supplier<PostEffectType<RyoikiTenkaiConfig>> typeSupplier;

        Type(Supplier<PostEffectType<RyoikiTenkaiConfig>> typeSupplier) {
            this.typeSupplier = typeSupplier;
        }

        public PostEffectType<RyoikiTenkaiConfig> get() {
            return typeSupplier.get();
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
