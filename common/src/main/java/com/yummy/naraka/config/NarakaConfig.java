package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.Color;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class NarakaConfig {
    public static final NarakaConfig INSTANCE = new NarakaConfig();

    private @Nullable WatchService watchService;
    private final ConfigFile file = new PropertiesConfig("naraka-common");
    private final Map<String, ConfigValue<?>> configuration = new LinkedHashMap<>();

    public final ConfigValue<Boolean> generatePillarCaves;
    public final ConfigValue<Boolean> showReinforcementValue;
    public final ConfigValue<Boolean> disableNonShaderLonginusRendering;
    public final ConfigValue<Color> afterimageColor;
    public final ConfigValue<Color> shadowHerobrineColor;
    public final ConfigValue<Boolean> showTestCreativeModeTab;
    public final ConfigValue<Integer> herobrineTakingStigmaTick;
    public final ConfigValue<Double> herobrineHurtLimitCalculationRatioModifier;
    public final ConfigValue<Double> herobrineMaxHurtCountCalculationModifier;
    public final ConfigValue<Integer> maxShadowHerobrineSpawn;
    public final ConfigValue<Integer> herobrineScarfPartitionNumber;
    public final ConfigValue<Float> herobrineScarfWaveSpeed;
    public final ConfigValue<Float> herobrineScarfWaveMaxAngle;
    public final ConfigValue<Float> herobrineScarfWaveCycleModifier;

    private boolean watchChange = true;

    public static void load() {

    }

    public void stop() {
        try {
            if (watchService != null)
                watchService.close();
        } catch (IOException ignored) {

        }
    }

    private void updateOnConfigChanges() {
        if (watchService == null)
            return;
        try {
            WatchKey watchKey;
            while ((watchKey = watchService.take()) != null) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    if (watchChange && event.context().toString().equals(INSTANCE.file.getFileName()))
                        INSTANCE.loadValues();
                }
                watchKey.reset();
            }
        } catch (ClosedWatchServiceException | InterruptedException ignored) {

        }
    }

    private NarakaConfig() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            ConfigFile.CONFIG_PATH.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            Thread thread = new Thread(this::updateOnConfigChanges);
            thread.start();
        } catch (IOException e) {
            NarakaMod.LOGGER.warn("Cannot watch config directory ({})", ConfigFile.CONFIG_PATH);
        }

        this.generatePillarCaves = define("generate_pillar_caves", false);
        this.showReinforcementValue = define("show_reinforcement_value", false);
        this.disableNonShaderLonginusRendering = define("disable_non_shader_longinus_rendering", false);
        this.afterimageColor = define("afterimage_color", Color.of(0x7e00ff));
        this.shadowHerobrineColor = define("shadow_herobrine_color", Color.of(0x0000ff));
        this.showTestCreativeModeTab = define("show_test_creative_mode_tab", false);
        this.herobrineTakingStigmaTick = define("herobrine_taking_stigma_tick", 1200);
        this.herobrineHurtLimitCalculationRatioModifier = define("herobrine_hurt_limit_calculation_ratio_modifier", 1.0)
                .append("Bigger value, bigger hurt limit");
        this.herobrineMaxHurtCountCalculationModifier = define("herobrine_max_hurt_count_calculation_modifier", 1.0)
                .append("Bigger value, bigger max hurt count");
        this.maxShadowHerobrineSpawn = define("max_shadow_herobrine_spawn", 3);
        this.herobrineScarfPartitionNumber = define("herobrine_scarf_partition_number", 16);
        this.herobrineScarfWaveSpeed = define("herobrine_scarf_wave_speed", 0.3f);
        this.herobrineScarfWaveMaxAngle = define("herobrine_scarf_wave_max_angle", 22.5f);
        this.herobrineScarfWaveCycleModifier = define("herobrine_scarf_wave_cycle_modifier", 1.0f)
                .append("Bigger value, short wave cycle");

        loadValues();
    }

    private <T> ConfigValue<T> define(String key, T defaultValue) {
        ConfigValue<T> value = new ConfigValue<>(defaultValue);
        configuration.put(key, value);
        return value;
    }

    private synchronized void loadValues() {
        try (Reader reader = file.createReader()) {
            boolean hasMissing = false;
            for (Map.Entry<String, ConfigValue<?>> entry : configuration.entrySet()) {
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

    private synchronized void saveValues() {
        watchChange = false;
        try (Writer writer = file.createWriter()) {
            for (Map.Entry<String, ConfigValue<?>> entry : configuration.entrySet()) {
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
