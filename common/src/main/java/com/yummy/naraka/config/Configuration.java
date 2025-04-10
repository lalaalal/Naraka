package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class Configuration {
    private final ConfigFile file;
    private final Map<String, ConfigValue<?>> configurations = new LinkedHashMap<>();

    private boolean watchChange = true;

    protected Configuration(String name, Function<String, ConfigFile> configFileFactory) {
        this.file = configFileFactory.apply(name);
    }

    public boolean canUpdateOnFileChange(String fileName) {
        return watchChange && file.getFileName().equals(fileName);
    }

    protected <T> ConfigValue<T> define(String key, T defaultValue) {
        ConfigValue<T> value = new ConfigValue<>(defaultValue);
        configurations.put(key, value);
        return value;
    }

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

    public synchronized void saveValues() {
        watchChange = false;
        try (Writer writer = file.createWriter()) {
            for (Map.Entry<String, ConfigValue<?>> entry : configurations.entrySet()) {
                String key = entry.getKey();
                ConfigValue<?> value = entry.getValue();
                file.write(writer, key, value);
            }
            writer.flush();
        } catch (IOException exception) {
            watchChange = true;
            NarakaMod.LOGGER.error("An error occurred while saving config values");
            throw new RuntimeException(exception);
        }
        watchChange = true;
    }

    public static class ConfigValue<T> {
        private final Class<T> type;
        private final List<String> comments = new ArrayList<>();
        private final T defaultValue;
        private T value;

        public ConfigValue(T defaultValue) {
            this(defaultValue, defaultValue);
        }

        @SuppressWarnings("unchecked")
        public ConfigValue(T defaultValue, T value) {
            this.type = (Class<T>) defaultValue.getClass();
            this.defaultValue = defaultValue;
            this.value = value;
        }

        public ConfigValue<T> set(T value) {
            this.value = value;
            return this;
        }

        public ConfigValue<T> append(String comment) {
            this.comments.add(comment);
            return this;
        }

        public List<String> getComments() {
            return comments;
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        public T getValue() {
            return value;
        }

        public Class<T> getType() {
            return type;
        }
    }
}
