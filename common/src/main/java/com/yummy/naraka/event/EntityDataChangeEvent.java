package com.yummy.naraka.event;

import com.yummy.naraka.world.entity.data.EntityDataType;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityDataChangeEvent {
    private final Map<EntityDataType<?, ?>, Event<? extends EntityDataChange<?, ?>>> listeners = new HashMap<>();

    private static <T, E extends Entity> Event<EntityDataChange<T, E>> createEvent() {
        return Event.create(listeners -> (entity, from, to) -> {
            for (EntityDataChange<T, E> listener : listeners) {
                listener.onChange(entity, from, to);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T, E extends Entity> Event<EntityDataChange<T, E>> event(EntityDataType<T, E> type) {
        return (Event<EntityDataChange<T, E>>) listeners.computeIfAbsent(type, key -> EntityDataChangeEvent.createEvent());
    }

    public <T, E extends Entity> void register(EntityDataType<T, E> type, EntityDataChange<T, E> listener) {
        event(type).register(listener);
    }

    public <T, E extends Entity> EntityDataChange<T, E> invoker(EntityDataType<T, E> type) {
        return event(type).invoker();
    }

    @FunctionalInterface
    public interface EntityDataChange<T, E extends Entity> {
        void onChange(E entity, T from, T to);
    }
}
