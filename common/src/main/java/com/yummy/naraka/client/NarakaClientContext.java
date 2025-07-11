package com.yummy.naraka.client;

import com.yummy.naraka.config.Configuration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class NarakaClientContext {
    public static final Configuration.ConfigValue<Boolean> SHADER_ENABLED = new Configuration.ConfigValue<>(false);
    public static final Configuration.ConfigValue<Boolean> ENABLE_HEROBRINE_SKY = new Configuration.ConfigValue<>(false);
    public static final Configuration.ConfigValue<Boolean> ENABLE_WHITE_SCREEN = new Configuration.ConfigValue<>(false);
    public static final Configuration.ConfigValue<Integer> CAMERA_SHAKE_TICK = new Configuration.ConfigValue<>(0);
}
