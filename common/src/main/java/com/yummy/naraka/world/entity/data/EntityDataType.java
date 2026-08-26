package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import net.minecraft.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class EntityDataType<T, E extends Entity> {
    private final ResourceLocation id;
    private final Supplier<EntityData<T, E>> defaultInstance;
    private final MapCodec<EntityData<T, E>> mapCodec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, EntityData<T, E>> streamCodec;
    private final boolean synchronize;
    private final ClientTicker<T, E> clientTicker;
    private final ServerTicker<T, E> serverTicker;
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

    private EntityDataType(ResourceLocation id, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean synchronize, Class<E> entityType, Function<EntityDataType<T, E>, EntityData<T, E>> defaultInstance, ClientTicker<T, E> clientTicker, ServerTicker<T, E> serverTicker) {
        this.id = id;
        this.defaultInstance = () -> defaultInstance.apply(this);
        this.entityType = entityType;
        this.mapCodec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        codec.fieldOf("value").forGetter(EntityData::value)
                ).apply(instance, value -> new EntityData<>(this, value))
        );
        this.streamCodec = streamCodec
                .map(value -> new EntityData<>(this, value), EntityData::value);
        this.synchronize = synchronize;
        this.clientTicker = clientTicker;
        this.serverTicker = serverTicker;
    }

    public ResourceLocation getId() {
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

    public StreamCodec<? super RegistryFriendlyByteBuf, EntityData<T, E>> streamCodec() {
        return streamCodec;
    }

    public boolean shouldSynchronize() {
        return synchronize;
    }

    public void tick(Entity entity) {
        getCastedTarget(entity).ifPresent(concreteEntity -> {
            T data = EntityDataHelper.getRawEntityData(concreteEntity, this);
            if (concreteEntity.level() instanceof ServerLevel serverLevel) {
                serverTicker.tick(serverLevel, concreteEntity, data);
            } else {
                clientTicker.tick(concreteEntity.level(), concreteEntity, data);
            }
        });
    }

    public boolean isValidTarget(Entity entity) {
        return entityType.isInstance(entity);
    }

    public Optional<E> getCastedTarget(Entity entity) {
        if (isValidTarget(entity))
            return Optional.of(entityType.cast(entity));
        return Optional.empty();
    }

    public interface ClientTicker<T, E extends Entity> {
        static <T, E extends Entity> ClientTicker<T, E> empty() {
            return (level, livingEntity, data) -> {
            };
        }

        void tick(Level level, E livingEntity, T data);
    }

    public interface ServerTicker<T, E extends Entity> {
        static <T, E extends Entity> ServerTicker<T, E> empty() {
            return (level, livingEntity, data) -> {
            };
        }

        void tick(ServerLevel level, E livingEntity, T data);
    }

    public static class Builder<T, E extends Entity> {
        private final Codec<T> codec;
        private final Class<E> entityType;
        private ResourceLocation id;
        @Nullable
        private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
        @Nullable
        private Function<EntityDataType<T, E>, EntityData<T, E>> defaultInstance;
        private ClientTicker<T, E> clientTicker;
        private ServerTicker<T, E> serverTicker;

        private Builder(Codec<T> codec, Class<E> entityType) {
            this.id = NarakaMod.location("empty");
            this.codec = codec;
            this.entityType = entityType;
            this.clientTicker = ClientTicker.empty();
            this.serverTicker = ServerTicker.empty();
        }

        public Builder<T, E> id(ResourceLocation id) {
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

        public Builder<T, E> clientTicker(ClientTicker<T, E> clientTicker) {
            this.clientTicker = clientTicker;
            return this;
        }

        public Builder<T, E> serverTicker(ServerTicker<T, E> serverTicker) {
            this.serverTicker = serverTicker;
            return this;
        }

        public Builder<T, E> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        public EntityDataType<T, E> build() {
            if (defaultInstance == null)
                throw new IllegalStateException("Default value must be set");
            boolean synchronize = (streamCodec != null);
            if (!synchronize)
                streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec);
            return new EntityDataType<>(id, codec, streamCodec, synchronize, entityType, defaultInstance, clientTicker, serverTicker);
        }
    }
}
