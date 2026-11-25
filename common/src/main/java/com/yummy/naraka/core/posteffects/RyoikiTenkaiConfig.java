package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public record RyoikiTenkaiConfig(Type ryoikiType, float restoreStartTime,
                                 float restoreDuration) implements PostEffectConfig {
    public static final Codec<RyoikiTenkaiConfig> MAP_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Type.CODEC.fieldOf("ryoiki_type").forGetter(RyoikiTenkaiConfig::ryoikiType),
                    Codec.FLOAT.fieldOf("restore_start_time").forGetter(RyoikiTenkaiConfig::restoreStartTime),
                    Codec.FLOAT.fieldOf("restore_duration").forGetter(RyoikiTenkaiConfig::restoreDuration)
            ).apply(instance, RyoikiTenkaiConfig::new)
    );

    public static RyoikiTenkaiConfig green(float restoreStartTime, float restoreDuration) {
        return new RyoikiTenkaiConfig(Type.GREEN, restoreStartTime, restoreDuration);
    }

    public static RyoikiTenkaiConfig gray(float restoreStartTime, float restoreDuration) {
        return new RyoikiTenkaiConfig(Type.GRAY, restoreStartTime, restoreDuration);
    }

    @Override
    public DynamicPostEffectType<?> type() {
        return ryoikiType.get();
    }

    public enum Type implements StringRepresentable {
        GREEN(() -> NarakaDynamicPostEffectTypes.RYOIKI_GREEN),
        GRAY(() -> NarakaDynamicPostEffectTypes.RYOIKI_GRAY);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final Supplier<? extends Holder<DynamicPostEffectType<?>>> typeSupplier;

        Type(Supplier<? extends Holder<DynamicPostEffectType<?>>> typeSupplier) {
            this.typeSupplier = typeSupplier;
        }

        public DynamicPostEffectType<?> get() {
            return typeSupplier.get().value();
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
