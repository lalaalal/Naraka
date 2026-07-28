package com.yummy.naraka.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Configuration whose key set is not fixed.
 * Value type is fixed.
 *
 * @param <T> Type of configuration value
 */
public abstract class DynamicConfiguration<T> extends Configuration {
    protected final List<ConfigValue<T>> configurations = new ArrayList<>();
    private final List<ConfigValue<T>> defaultValues = new ArrayList<>();

    public DynamicConfiguration(String name, Function<String, ConfigFile> configFileFactory) {
        super(name, configFileFactory);
    }

    /**
     * Set default value.
     *
     * @param key          Key of configuration value
     * @param defaultValue Default configuration value
     */
    protected void addDefaultValue(String key, T defaultValue) {
        ConfigValue<T> configValue = new ConfigValue<>(key, defaultValue);
        this.defaultValues.add(configValue);
        this.configurations.add(configValue);
    }

    /**
     * Default value for each existing key.
     *
     * @return New configuration value with default value
     */
    protected abstract ConfigValue<T> createDefaultValue(String key);

    @Override
    public Collection<ConfigValue<T>> values() {
        return configurations;
    }

    public Collection<ConfigValue<T>> defaultValues() {
        return defaultValues;
    }

    public void loadValues(Collection<ConfigValue<T>> values) {
        configurations.clear();
        configurations.addAll(values);
    }

    @Override
    public synchronized void internalLoadValues() throws IOException {
        configurations.clear();
        for (String key : file.load()) {
            ConfigValue<T> value = createDefaultValue(key);
            file.read(key, value);
            configurations.add(value);
        }
    }
}
