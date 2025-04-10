package com.yummy.naraka.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Configuration {
    protected final ConfigFile file;
    protected boolean watchChange = true;

    public Configuration(String name, Function<String, ConfigFile> configFileFactory) {
        this.file = configFileFactory.apply(name);
    }

    public abstract boolean canUpdateOnFileChange(String fileName);

    public abstract void loadValues();

    public abstract void saveValues();

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

        public ConfigValue<T> comment(String comment) {
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
