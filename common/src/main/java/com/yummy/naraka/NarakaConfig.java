package com.yummy.naraka;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yummy.naraka.util.NarakaJsonUtils;
import com.yummy.naraka.util.Platform;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class NarakaConfig {
    public static final NarakaConfig INSTANCE = new NarakaConfig();

    protected final Map<String, ConfigValue<?>> configuration = new HashMap<>();
    public final ConfigValue<Boolean> generatePillarCaves;

    protected static void load() {

    }

    protected NarakaConfig() {
        this.generatePillarCaves = define("generate_pillar_caves", false);

        loadValues();
    }

    protected <T> ConfigValue<T> define(String path, T defaultValue) {
        ConfigValue<T> value = new ConfigValue<>(defaultValue);
        configuration.put(path, value);
        return value;
    }

    protected void loadValues() {
        Path configPath = Platform.getInstance().getConfigurationPath().resolve("naraka-common.json");
        try (Reader reader = new FileReader(configPath.toFile())) {
            JsonObject jsonObject = NarakaJsonUtils.GSON.fromJson(reader, JsonObject.class);
            jsonObject.keySet().forEach(key -> {
                JsonElement element = jsonObject.get(key);
                configuration.get(key).load(element);
            });
        } catch (FileNotFoundException exception) {
            saveValues();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void saveValues() {
        Path configPath = Platform.getInstance().getConfigurationPath().resolve("naraka-common.json");
        try (Writer writer = new FileWriter(configPath.toFile())) {
            NarakaJsonUtils.GSON.toJson(configuration, writer);
            writer.flush();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static class ConfigValue<T> {
        private final T defaultValue;
        private T value;

        public ConfigValue(T defaultValue) {
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }

        public ConfigValue(T defaultValue, T value) {
            this.defaultValue = defaultValue;
            this.value = value;
        }

        public ConfigValue<T> set(T value) {
            this.value = value;
            return this;
        }

        @SuppressWarnings("unchecked")
        public void load(JsonElement jsonElement) {
            Class<T> type = (Class<T>) value.getClass();
            this.value = NarakaJsonUtils.parser(type, jsonElement);
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        public T getValue() {
            return value;
        }
    }
}
