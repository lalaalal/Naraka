package com.yummy.naraka.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @param <T> Type to invoke when this event occurs
 * @see #create(Function)
 */
public abstract class Event<T> {
    protected final List<T> listeners = new ArrayList<>();

    /**
     * Create a simple event instance.
     * <blockquote><pre>
     * create(listeners -> (args) -> {
     *     for (T listener : listeners) {
     *         listener.invoke(args);
     *     }
     *     // return value of type T if not void
     * });
     * </pre></blockquote>
     *
     * @param invokerFactory A function handling the event from {@link Event#listeners}
     * @param <T>            Type to invoke when this event occurs
     * @return New event
     */
    public static <T> Event<T> create(Function<List<T>, T> invokerFactory) {
        return new SimpleEvent<>(invokerFactory);
    }

    protected Event() {
    }

    /**
     * @return Select or iterate invoker from {@link Event#listeners}
     */
    public abstract T invoker();

    /**
     * Add a new invoker for this event.
     *
     * @param listener Instance to invoke when this event occurs
     */
    public void register(T listener) {
        listeners.add(listener);
    }

    private static class SimpleEvent<T> extends Event<T> {
        private final T invoker;

        public SimpleEvent(Function<List<T>, T> invokerFactory) {
            this.invoker = invokerFactory.apply(listeners);
        }

        public T invoker() {
            if (listeners.size() == 1)
                return listeners.getFirst();
            return this.invoker;
        }
    }
}
