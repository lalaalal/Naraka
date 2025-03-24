package com.yummy.naraka.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Event<T> {
    protected final List<T> listeners = new ArrayList<>();

    public static <T> Event<T> create(Function<List<T>, T> invokerFactory) {
        return new Event<>() {
            @Override
            public T invoker() {
                if (listeners.size() == 1)
                    return listeners.getFirst();
                return invokerFactory.apply(listeners);
            }
        };
    }

    protected Event() {

    }

    public abstract T invoker();

    public void register(T listener) {
        listeners.add(listener);
    }
}
