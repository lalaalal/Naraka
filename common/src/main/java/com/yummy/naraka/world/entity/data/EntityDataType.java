package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class EntityDataType<T, E extends Entity> {
    private final Identifier id;
    private final Supplier<EntityData<T, E>> defaultInstance;
    private final MapCodec<EntityData<T, E>> mapCodec;
    private final StreamCodec<RegistryFriendlyByteBuf, EntityData<T, E>> streamCodec;
    private final BiConsumer<E, T> ticker;
    private final Class<E> entityType;

    public static <T, E extends Entity> Builder<T, E> builder(Codec<T> codec, Class<E> entityType) {
        return new Builder<>(codec, entityType);
    }

    public static <T> Builder<T, Entity> common(Codec<T> codec) {
        return builder(codec, Entity.class);
    }

    public static <T> Builder<T, LivingEntity> living(Codec<T> codec) {
        return builder(codec, LivingEntity.class);
    }

    private EntityDataType(Identifier id, Codec<T> codec, Class<E> entityType, Function<EntityDataType<T, E>, EntityData<T, E>> defaultInstance, BiConsumer<E, T> ticker) {
        this.id = id;
        this.defaultInstance = () -> defaultInstance.apply(this);
        this.entityType = entityType;
        this.mapCodec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        codec.fieldOf("value").forGetter(EntityData::value)
                ).apply(instance, value -> new EntityData<>(this, value))
        );
        this.streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec)
                .map(value -> new EntityData<>(this, value), EntityData::value);
        this.ticker = ticker;
    }

    public Identifier getId() {
        return id;
    }

    public T getDefaultValue() {
        return getDefault().value();
    }

    public EntityData<T, E> getDefault() {
        return defaultInstance.get();
    }

    public String name() {
        return getId().getPath();
    }

    public MapCodec<EntityData<T, E>> mapCodec() {
        return mapCodec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, EntityData<T, E>> streamCodec() {
        return streamCodec;
    }

    public void tick(Entity entity) {
        getCastedTarget(entity).ifPresent(target -> ticker.accept(
                target,
                EntityDataHelper.getRawEntityData(target, this)
        ));
    }

    public boolean isValidTarget(Entity entity) {
        return entityType.isInstance(entity);
    }

    public Optional<E> getCastedTarget(Entity entity) {
        if (isValidTarget(entity))
            return Optional.of(entityType.cast(entity));
        return Optional.empty();
    }

    public static class Builder<T, E extends Entity> {
        private final Codec<T> codec;
        private final Class<E> entityType;
        private Identifier id;
        @Nullable
        private Function<EntityDataType<T, E>, EntityData<T, E>> defaultInstance;
        private BiConsumer<E, T> ticker;

        private Builder(Codec<T> codec, Class<E> entityType) {
            this.id = NarakaMod.identifier("empty");
            this.codec = codec;
            this.entityType = entityType;
            this.ticker = (livingEntity, value) -> {
            };
        }

        public Builder<T, E> id(Identifier id) {
            this.id = id;
            return this;
        }

        public Builder<T, E> defaultValue(Supplier<T> defaultValue) {
            this.defaultInstance = type -> new EntityData<>(type, defaultValue.get());
            return this;
        }

        public Builder<T, E> defaultValue(T defaultValue) {
            this.defaultInstance = Util.memoize(type -> new EntityData<>(type, defaultValue));
            return this;
        }

        public Builder<T, E> ticker(BiConsumer<E, T> ticker) {
            this.ticker = ticker;
            return this;
        }

        public EntityDataType<T, E> build() {
            if (defaultInstance == null)
                throw new IllegalStateException("Default value must be set");
            return new EntityDataType<>(id, codec, entityType, defaultInstance, ticker);
        }
    }
}
