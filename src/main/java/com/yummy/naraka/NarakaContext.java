package com.yummy.naraka;

import java.util.HashMap;
import java.util.Map;

public class NarakaContext {
    public static final String KEY_CLIENT_DEATH_COUNT_VISIBILITY = "client.gui.death_count.visible";

    static final NarakaContext INSTANCE = new NarakaContext();

    private final Map<String, Boolean> contexts = new HashMap<>();

    private NarakaContext() {
    }

    public static void initialize() {
        INSTANCE.set(KEY_CLIENT_DEATH_COUNT_VISIBILITY, false);
    }

    public void set(String key, boolean value) {
        contexts.put(key, value);
    }

    public boolean get(String key) {
        if (contexts.containsKey(key))
            return contexts.get(key);
        throw new IllegalArgumentException("Key " + key + " not found");
    }
}
