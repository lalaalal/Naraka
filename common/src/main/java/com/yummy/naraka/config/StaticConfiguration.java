package com.yummy.naraka.config;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Configuration whose key set is fixed.
 */
public abstract class StaticConfiguration extends Configuration {
    private static final Logger LOG = LogUtils.getLogger();

    protected final List<ConfigValue<?>> configurations = new ArrayList<>();

    protected StaticConfiguration(String name, Function<String, ConfigFile> configFileFactory) {
        super(name, configFileFactory);
    }

    /**
     * Defines configuration must be set.
     *
     * @param key          Key of configuration
     * @param defaultValue Default value
     * @param <T>          Type of configuration value
     * @return Configuration value instance
     */
    protected <T> ConfigValue<T> define(String key, T defaultValue) {
        ConfigValue<T> value = new ConfigValue<>(key, defaultValue);
        configurations.add(value);
        return value;
    }

    @Override
    public Collection<ConfigValue<?>> values() {
        return configurations;
    }

    @Override
    public synchronized void internalLoadValues() throws IOException {
        file.load();
        int counter = 0;
        for (ConfigValue<?> value : configurations) {
            file.read(value);
            if (file.contains(value))
                counter += 1;
        }
        if (counter < configurations.size()) {
            LOG.warn("Missing config values found. Rewriting config \"{}\"", name);
            saveValues();
        }
    }
}
