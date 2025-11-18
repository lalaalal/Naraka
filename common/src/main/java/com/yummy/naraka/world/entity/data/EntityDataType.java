package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class EntityDataType<T> {
    private final ResourceLocation id;
    private final Supplier<T> defaultValue;
    private final MapCodec<EntityData<T>> mapCodec;
    private final StreamCodec<RegistryFriendlyByteBuf, EntityData<T>> streamCodec;
    private final Consumer<LivingEntity> ticker;

    public static <T> Builder<T> builder(Codec<T> codec) {
        return new Builder<>(codec);
    }

    public static <T> Builder<T> simple(Codec<T> codec, T defaultValue) {
        return builder(codec).defaultValue(defaultValue);
    }

    public EntityDataType(ResourceLocation id, Codec<T> codec, Supplier<T> defaultValue, Consumer<LivingEntity> ticker) {
        this.id = id;
        this.defaultValue = defaultValue;
        this.mapCodec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        codec.fieldOf("value").forGetter(EntityData::value)
                ).apply(instance, value -> new EntityData<>(this, value))
        );
        this.streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec)
                .map(value -> new EntityData<>(this, value), EntityData::value);
        this.ticker = ticker;
    }

    public ResourceLocation getId() {
        return id;
    }

    public T getDefaultValue() {
        return defaultValue.get();
    }

    public String name() {
        return getId().getPath();
    }

    public MapCodec<EntityData<T>> mapCodec() {
        return mapCodec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, EntityData<T>> streamCodec() {
        return streamCodec;
    }

    public void tick(LivingEntity livingEntity) {
        ticker.accept(livingEntity);
    }

    public static class Builder<T> {
        private final Codec<T> codec;
        private ResourceLocation id;
        @Nullable
        private Supplier<T> defaultValue;
        private Consumer<LivingEntity> ticker;

        private Builder(Codec<T> codec) {
            this.id = NarakaMod.location("empty");
            this.codec = codec;
            this.ticker = livingEntity -> {
            };
        }

        public Builder<T> id(ResourceLocation id) {
            this.id = id;
            return this;
        }

        public Builder<T> defaultValue(Supplier<T> defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = () -> defaultValue;
            return this;
        }

        public Builder<T> ticker(Consumer<LivingEntity> ticker) {
            this.ticker = ticker;
            return this;
        }

        public EntityDataType<T> build() {
            if (defaultValue == null)
                throw new IllegalStateException("Default value must be set");
            return new EntityDataType<>(id, codec, defaultValue, ticker);
        }
    }
}
