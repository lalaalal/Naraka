package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class StaticConfiguration extends Configuration {
    protected final Map<String, ConfigValue<?>> configurations = new LinkedHashMap<>();

    protected StaticConfiguration(String name, Function<String, ConfigFile> configFileFactory) {
        super(name, configFileFactory);
    }

    @Override
    public boolean canUpdateOnFileChange(String fileName) {
        return watchChange && file.getFileName().equals(fileName);
    }

    protected <T> ConfigValue<T> define(String key, T defaultValue) {
        ConfigValue<T> value = new ConfigValue<>(defaultValue);
        configurations.put(key, value);
        return value;
    }

    @Override
    public synchronized void loadValues() {
        try (Reader reader = file.createReader()) {
            boolean hasMissing = false;
            for (Map.Entry<String, ConfigValue<?>> entry : configurations.entrySet()) {
                String key = entry.getKey();
                ConfigValue<?> value = entry.getValue();
                if (!file.contains(reader, key))
                    hasMissing = true;
                file.read(reader, key, value);
            }
            if (hasMissing)
                saveValues();
        } catch (FileNotFoundException exception) {
            saveValues();
        } catch (IOException exception) {
            NarakaMod.LOGGER.error("An error occurred while loading config values");
            throw new RuntimeException(exception);
        }
    }

    @Override
    public synchronized void saveValues() {
        watchChange = false;
        try (Writer writer = file.createWriter()) {
            for (Map.Entry<String, ConfigValue<?>> entry : configurations.entrySet()) {
                String key = entry.getKey();
                ConfigValue<?> value = entry.getValue();
                file.write(writer, key, value);
            }
            file.commit(writer);
        } catch (IOException exception) {
            watchChange = true;
            NarakaMod.LOGGER.error("An error occurred while saving config values");
            throw new RuntimeException(exception);
        }
        watchChange = true;
    }

}
