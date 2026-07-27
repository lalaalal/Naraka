package com.yummy.naraka.config;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;
import java.util.function.Function;

/**
 * Configuration whose key set is not fixed.
 * Value type is fixed.
 *
 * @param <T> Type of configuration value
 */
public abstract class DynamicConfiguration<T> extends Configuration {
    private static final Logger LOG = LogUtils.getLogger();

    protected final Map<String, ConfigValue<T>> configurations = new LinkedHashMap<>();
    private final Map<String, ConfigValue<T>> defaultValues = new LinkedHashMap<>();

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
        this.defaultValues.put(key, new ConfigValue<>(key, defaultValue));
    }

    /**
     * Default value for each existing key.
     *
     * @return New configuration value with default value
     */
    protected abstract ConfigValue<T> createDefaultValue(String key);

    @Override
    public Collection<ConfigValue<T>> values() {
        return configurations.values();
    }

    public Collection<ConfigValue<T>> defaultValues() {
        return defaultValues.values();
    }

    public void loadValues(List<ConfigValue<T>> values) {
        configurations.clear();
        for (ConfigValue<T> value : values)
            configurations.put(value.getKey(), value);
    }

    @Override
    public synchronized void loadValues() {
        LOG.info("Loading dynamic configuration \"{}\"", name);
        configurations.clear();
        try (Reader reader = file.createReader()) {
            for (String key : file.load(reader)) {
                ConfigValue<T> value = createDefaultValue(key);
                file.read(key, value);
                configurations.put(key, value);
            }
        } catch (FileNotFoundException exception) {
            LOG.warn("Configuration file \"{}\" is not found", file.getFileName());
            saveValues();
            loadValues();
        } catch (IOException exception) {
            LOG.error("An error occurred while loading config values");
        }
    }

    @Override
    public synchronized void saveValues() {
        LOG.info("Saving dynamic configuration \"{}\" to \"{}\"", name, file.getAbsolutePath());
        try (Writer writer = file.createWriter()) {
            for (Map.Entry<String, ConfigValue<T>> entry : entries())
                file.appendToBuffer(entry.getKey(), entry.getValue());
            file.commit(writer);
        } catch (IOException exception) {
            LOG.error("An error occurred while saving default configuration values for \"{}\"", name);
            LOG.warn("Ignore all configuration values for \"{}\"", name);
        }
    }

    private Set<Map.Entry<String, ConfigValue<T>>> entries() {
        if (configurations.isEmpty())
            return defaultValues.entrySet();
        return configurations.entrySet();
    }
}
