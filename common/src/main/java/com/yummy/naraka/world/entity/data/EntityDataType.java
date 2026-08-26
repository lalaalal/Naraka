package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import net.minecraft.Util;
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
    private final Codec<EntityData<T, E>> codec;
    private final ClientTicker<T, E> clientTicker;
    private final ServerTicker<T, E> serverTicker;
    private final Class<E> entityType;
    private final boolean synchronize;

    public static <T, E extends Entity> Builder<T, E> builder(Codec<T> codec, Class<E> entityType) {
        return new Builder<>(codec, entityType);
    }

    public static <T> Builder<T, Entity> common(Codec<T> codec) {
        return builder(codec, Entity.class);
    }

    public static <T> Builder<T, LivingEntity> living(Codec<T> codec) {
        return builder(codec, LivingEntity.class);
    }

    private EntityDataType(ResourceLocation id, Codec<T> codec, Class<E> entityType, Function<EntityDataType<T, E>, EntityData<T, E>> defaultInstance, ClientTicker<T, E> clientTicker, ServerTicker<T, E> serverTicker, boolean synchronize) {
        this.id = id;
        this.defaultInstance = () -> defaultInstance.apply(this);
        this.entityType = entityType;
        this.codec = RecordCodecBuilder.create(instance -> instance.group(
                        codec.fieldOf("value").forGetter(EntityData::value)
                ).apply(instance, value -> new EntityData<>(this, value))
        );
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

    public Codec<EntityData<T, E>> codec() {
        return codec;
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
        private Function<EntityDataType<T, E>, EntityData<T, E>> defaultInstance;
        private ClientTicker<T, E> clientTicker;
        private ServerTicker<T, E> serverTicker;
        private boolean synchronize;

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

        public Builder<T, E> sync(boolean sync) {
            this.synchronize = sync;
            return this;
        }

        public EntityDataType<T, E> build() {
            if (defaultInstance == null)
                throw new IllegalStateException("Default value must be set");
            return new EntityDataType<>(id, codec, entityType, defaultInstance, ticker, synchronize);
        }
    }
}
