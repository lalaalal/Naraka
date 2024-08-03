package com.yummy.naraka;

import java.util.HashMap;
import java.util.Map;

/**
 * Context of mod<br>
 * Supports boolean, number for value type
 *
 * @author lalaalal
 * @see NarakaContext#get(String, Class)
 */
public class NarakaContext {
    static final NarakaContext INSTANCE = new NarakaContext();

    private final Map<Class<?>, Map<String, ?>> contextMaps = new HashMap<>();
    private final Map<String, Boolean> booleanContexts = new HashMap<>();
    private final Map<String, Number> numberContexts = new HashMap<>();

    private NarakaContext() {
        contextMaps.put(Boolean.class, booleanContexts);
        contextMaps.put(Number.class, numberContexts);
    }

    public static void initialize() {

    }

    public void set(String key, boolean value) {
        booleanContexts.put(key, value);
    }

    public void set(String key, Number value) {
        numberContexts.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        if (contextMaps.containsKey(type)) {
            Object value = contextMaps.get(type).get(key);
            if (type.isInstance(value))
                return type.cast(value);
        }
        throw new IllegalArgumentException("Key " + key + " not found");
    }
}
