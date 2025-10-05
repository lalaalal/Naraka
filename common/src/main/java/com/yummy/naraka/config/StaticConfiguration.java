package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Configuration whose key set is fixed.
 */
public abstract class StaticConfiguration extends Configuration {
    protected final Map<String, ConfigValue<?>> configurations = new LinkedHashMap<>();

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
        ConfigValue<T> value = new ConfigValue<>(defaultValue);
        configurations.put(key, value);
        return value;
    }

    @Override
    public synchronized void loadValues() {
        NarakaMod.LOGGER.debug("Loading static configuration \"{}\"", name);
        try (Reader reader = file.createReader()) {
            file.load(reader);
            AtomicInteger counter = new AtomicInteger(0);
            configurations.forEach((key, configValue) -> {
                file.read(key, configValue);
                if (file.contains(key))
                    counter.addAndGet(1);
            });
            if (counter.get() < configurations.size())
                saveValues();
        } catch (FileNotFoundException exception) {
            NarakaMod.LOGGER.warn("Configuration file \"{}\" is not found", file.getFileName());
            saveValues();
        } catch (IOException exception) {
            NarakaMod.LOGGER.error("An error occurred while loading config values");
            NarakaMod.LOGGER.warn("Using default values for configuration \"{}\"", name);
        }
    }

    @Override
    public synchronized void saveValues() {
        NarakaMod.LOGGER.debug("Saving static configuration \"{}\" to \"{}\"", name, file.getAbsolutePath());
        watchChange = false;
        try (Writer writer = file.createWriter()) {
            for (Map.Entry<String, ConfigValue<?>> entry : configurations.entrySet()) {
                String key = entry.getKey();
                ConfigValue<?> value = entry.getValue();
                file.write(writer, key, value);
            }
            file.commit(writer);
        } catch (IOException exception) {
            NarakaMod.LOGGER.error("An error occurred while saving config values");
        } finally {
            watchChange = true;
        }
    }

}
