package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Configuration whose key set is not fixed.
 * Value type is fixed.
 *
 * @param <T> Type of configuration value
 */
public abstract class DynamicConfiguration<T> extends Configuration {
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
    protected void addDefaultValue(String key, ConfigValue<T> defaultValue) {
        this.defaultValues.put(key, defaultValue);
    }

    /**
     * Default value for each existing key.
     *
     * @return New configuration value with default value
     */
    protected abstract ConfigValue<T> createDefaultValue();

    @Override
    public void loadValues() {
        NarakaMod.LOGGER.info("Loading dynamic configuration \"{}\"", name);
        configurations.clear();
        try (Reader reader = file.createReader()) {
            for (String key : file.load(reader)) {
                ConfigValue<T> value = createDefaultValue();
                file.read(key, value);
                configurations.put(key, value);
            }
        } catch (FileNotFoundException exception) {
            NarakaMod.LOGGER.warn("Configuration file \"{}\" is not found", file.getFileName());
            saveValues();
            loadValues();
        } catch (IOException exception) {
            NarakaMod.LOGGER.error("An error occurred while loading config values");
        }
    }

    @Override
    public void saveValues() {
        NarakaMod.LOGGER.info("Saving dynamic configuration \"{}\" to \"{}\"", name, file.getAbsolutePath());
        watchChange = false;
        try (Writer writer = file.createWriter()) {
            for (Map.Entry<String, ConfigValue<T>> entry : defaultValues.entrySet())
                file.write(writer, entry.getKey(), entry.getValue());
            file.commit(writer);
        } catch (IOException exception) {
            NarakaMod.LOGGER.error("An error occurred while saving default configuration values for \"{}\"", name);
            NarakaMod.LOGGER.warn("Ignore all configuration values for \"{}\"", name);
        } finally {
            watchChange = true;
        }
    }
}
