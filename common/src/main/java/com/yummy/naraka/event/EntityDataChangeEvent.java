package com.yummy.naraka.event;

import com.yummy.naraka.world.entity.data.EntityDataType;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityDataChangeEvent {
    private final Map<EntityDataType<?, ?>, Event<? extends EntityDataChange<?, ?>>> listeners = new HashMap<>();

    private static Event<EntityDataChange<Object, Entity>> createEvent() {
        return Event.create(listeners -> (entity, from, to) -> {
            for (EntityDataChange<Object, Entity> listener : listeners) {
                listener.onChange(entity, from, to);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T, E extends Entity> Event<EntityDataChange<T, E>> event(EntityDataType<T, E> type) {
        return (Event<EntityDataChange<T, E>>) listeners.computeIfAbsent(type, _ -> EntityDataChangeEvent.createEvent());
    }

    public <T, E extends Entity> void register(EntityDataType<T, E> type, EntityDataChange<T, E> listener) {
        event(type).register(listener);
    }

    @SuppressWarnings("unchecked")
    public EntityDataChange<Object, Entity> invoker(EntityDataType<?, ?> type) {
        return (EntityDataChange<Object, Entity>) listeners.computeIfAbsent(type, _ -> EntityDataChangeEvent.createEvent())
                .invoker();
    }

    @FunctionalInterface
    public interface EntityDataChange<T, E extends Entity> {
        void onChange(E entity, T from, T to);
    }
}
