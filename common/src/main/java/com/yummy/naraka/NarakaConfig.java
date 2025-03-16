package com.yummy.naraka;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yummy.naraka.util.NarakaJsonUtils;

import java.io.*;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

public final class NarakaConfig {
    private static final Path CONFIG_PATH = Platform.getInstance().getConfigurationPath();
    private static final Path CONFIG_FILE_PATH = CONFIG_PATH.resolve("naraka-common.json");

    public static final NarakaConfig INSTANCE = new NarakaConfig();

    private final Map<String, ConfigValue<?>> configuration = new LinkedHashMap<>();

    public final ConfigValue<Boolean> generatePillarCaves;
    public final ConfigValue<Boolean> showReinforcementValue;
    public final ConfigValue<Boolean> disableNonShaderLonginusRendering;
    public final ConfigValue<String> afterimageColor;
    public final ConfigValue<String> shadowHerobrineColor;
    public final ConfigValue<Boolean> showTestCreativeModeTab;
    public final ConfigValue<Integer> herobrineTakingStigmaTick;
    public final ConfigValue<Integer> hurtCountHerobrineEnterHibernatedMode;
    public final ConfigValue<Double> herobrineHurtLimitReduceFactor;
    public final ConfigValue<Double> herobrineMaxHurtCountReduceFactor;
    public final ConfigValue<Integer> maxShadowHerobrineSpawn;

    static void load() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            CONFIG_PATH.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            Thread thread = new Thread(() -> updateOnConfigChanges(watchService));
            thread.start();
        } catch (IOException ignored) {

        }
    }

    private static void updateOnConfigChanges(WatchService watchService) {
        try {
            WatchKey watchKey;
            while ((watchKey = watchService.take()) != null) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    if (event.context().equals(CONFIG_FILE_PATH.getFileName()))
                        INSTANCE.loadValues();
                }
                watchKey.reset();
            }
        } catch (ClosedWatchServiceException | InterruptedException ignored) {

        }
    }

    private NarakaConfig() {
        this.generatePillarCaves = define("generate_pillar_caves", false);
        this.showReinforcementValue = define("show_reinforcement_value", false);
        this.disableNonShaderLonginusRendering = define("disable_non_shader_longinus_rendering", false);
        this.afterimageColor = define("afterimage_color", "7e00ff");
        this.shadowHerobrineColor = define("shadow_herobrine_color", "0000ff");
        this.showTestCreativeModeTab = define("show_test_creative_mode_tab", false);
        this.herobrineTakingStigmaTick = define("herobrine_taking_stigma_tick", 1200);
        this.hurtCountHerobrineEnterHibernatedMode = define("hurt_count_herobrine_enter_hibernated_mode", 7);
        this.herobrineHurtLimitReduceFactor = define("herobrine_hurt_limit_reduce_factor", 13.0);
        this.herobrineMaxHurtCountReduceFactor = define("herobrine_max_hurt_count_reduce_factor", 0.01);
        this.maxShadowHerobrineSpawn = define("max_shadow_herobrine_spawn", 3);

        loadValues();
    }

    private <T> ConfigValue<T> define(String path, T defaultValue) {
        ConfigValue<T> value = new ConfigValue<>(defaultValue);
        configuration.put(path, value);
        return value;
    }

    private synchronized void loadValues() {
        try (Reader reader = new FileReader(CONFIG_FILE_PATH.toFile())) {
            JsonObject jsonObject = NarakaJsonUtils.GSON.fromJson(reader, JsonObject.class);
            int validKeys = 0;
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                if (configuration.containsKey(entry.getKey())) {
                    configuration.get(entry.getKey())
                            .load(entry.getValue());
                    validKeys += 1;
                }
            }
            if (validKeys < configuration.size())
                saveValues();
        } catch (FileNotFoundException exception) {
            saveValues();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private synchronized void saveValues() {
        try (Writer writer = new FileWriter(CONFIG_FILE_PATH.toFile())) {
            NarakaJsonUtils.GSON.toJson(configuration, writer);
            writer.flush();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static class ConfigValue<T> {
        private final Class<T> type;
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

        public void load(JsonElement jsonElement) {
            this.value = NarakaJsonUtils.parse(type, jsonElement);
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        public T getValue() {
            return value;
        }
    }
}
