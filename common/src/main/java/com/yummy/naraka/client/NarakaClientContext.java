package com.yummy.naraka.client;

import com.yummy.naraka.config.Configuration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class NarakaClientContext {
    private static final List<Configuration.ConfigValue<?>> CONTEXTS = new ArrayList<>();

    public static final Configuration.ConfigValue<Boolean> SHADER_ENABLED = context(false);
    public static final Configuration.ConfigValue<Boolean> ENABLE_HEROBRINE_SKY = context(false);
    public static final Configuration.ConfigValue<Boolean> ENABLE_WHITE_SCREEN = context(false);
    public static final Configuration.ConfigValue<Integer> CAMERA_SHAKE_TICK = context(0);

    private static <T> Configuration.ConfigValue<T> context(T defaultValue) {
        Configuration.ConfigValue<T> context = new Configuration.ConfigValue<>(defaultValue);
        CONTEXTS.add(context);
        return context;
    }

    public static void initialize() {
        CONTEXTS.forEach(Configuration.ConfigValue::reset);
    }
}
