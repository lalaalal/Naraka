package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@Deprecated
public class NeoForgeConfig extends NarakaConfig {
    public static final NeoForgeConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        Pair<NeoForgeConfig, ModConfigSpec> pair = BUILDER.configure(NeoForgeConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    private NeoForgeConfig(ModConfigSpec.Builder builder) {

    }

    @Override
    public <T> ConfigValue<T> define(String path, T defaultValue) {
        ModConfigSpec.ConfigValue<T> neoforgeValue = BUILDER.define(path, defaultValue);
        NarakaConfig.ConfigValue<T> narakaValue = new NeoForgeConfigValue<>(defaultValue, neoforgeValue);
        configuration.put(path, narakaValue);
        return narakaValue;
    }

    @Override
    protected void loadValues() {
        for (ConfigValue<?> value : configuration.values()) {
            if (value instanceof NeoForgeConfigValue<?> neoforgeValue)
                neoforgeValue.load();
        }
    }

    private static class NeoForgeConfigValue<T> extends ConfigValue<T> {
        private final ModConfigSpec.ConfigValue<T> neoforgeValue;

        public NeoForgeConfigValue(T defaultValue, ModConfigSpec.ConfigValue<T> neoforgeValue) {
            super(defaultValue);
            this.neoforgeValue = neoforgeValue;
        }

        public void load() {
            this.set(neoforgeValue.get());
        }
    }
}
