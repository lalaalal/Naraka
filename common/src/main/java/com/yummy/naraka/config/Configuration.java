package com.yummy.naraka.config;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * An abstract class provides an interface to load, save values
 */
public abstract class Configuration {
    private static final Logger LOG = LogUtils.getLogger();

    public final String name;
    protected final ConfigFile file;

    public Configuration(String name, Function<String, ConfigFile> configFileFactory) {
        this.name = name;
        this.file = configFileFactory.apply(name);
    }

    public String getFileName() {
        return file.getFileName();
    }

    public final synchronized void loadValues() {
        LOG.info("Loading configuration \"{}\"", name);
        if (!file.exists()) {
            LOG.info("Config file not found \"{}\"", name);
            saveValues();
            return;
        }
        try {
            internalLoadValues();
        } catch (IOException exception) {
            LOG.error("An error occurred while loading config values");
            LOG.warn("Using default values for configuration \"{}\"", name);
        }
    }

    protected abstract void internalLoadValues() throws IOException;

    public final synchronized void saveValues() {
        LOG.info("Saving configuration \"{}\" to \"{}\"", name, file.getAbsolutePath());
        try {
            file.prepareWrite();
            for (ConfigValue<?> value : values())
                file.appendToBuffer(value);
            file.commit();
        } catch (IOException exception) {
            LOG.error("An error occurred while saving config values");
        }
    }

    public abstract Collection<? extends ConfigValue<?>> values();

    /**
     * Configuration value instance which contains value, default value and comments.
     *
     * @param <T> Type of configuration value
     */
    public static class ConfigValue<T> {
        private final String key;
        private final Class<T> type;
        private final List<TranslatableContents> comments = new ArrayList<>();
        private final T defaultValue;
        private T value;

        public ConfigValue(T defaultValue) {
            this("empty", defaultValue, defaultValue);
        }

        public ConfigValue(String key, T defaultValue) {
            this(key, defaultValue, defaultValue);
        }

        @SuppressWarnings("unchecked")
        public ConfigValue(String key, T defaultValue, T value) {
            this.key = key;
            this.type = (Class<T>) defaultValue.getClass();
            this.defaultValue = defaultValue;
            this.value = value;
        }

        public ConfigValue<T> set(T value) {
            this.value = value;
            return this;
        }

        public ConfigValue<T> comment(String comment) {
            int index = comments.size();
            this.comments.add(new TranslatableContents(getCommentTranslationKey(index), comment, TranslatableContents.NO_ARGS));
            return this;
        }

        public String getKey() {
            return key;
        }

        public String getTranslationKey() {
            return "config.naraka." + key;
        }

        public Component getComponent() {
            return Component.translatable(getTranslationKey());
        }

        public String getCommentTranslationKey(int index) {
            return getTranslationKey() + ".comment." + index;
        }

        public List<String> getComments() {
            return comments.stream()
                    .map(TranslatableContents::getFallback)
                    .filter(Objects::nonNull)
                    .toList();
        }

        public List<MutableComponent> getCommentComponent() {
            return comments.stream()
                    .map(MutableComponent::create)
                    .toList();
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

        public void reset() {
            value = defaultValue;
        }
    }
}
