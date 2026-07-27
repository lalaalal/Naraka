package com.yummy.naraka.client.config;

import com.yummy.naraka.config.Configuration;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.util.Color;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class NarakaConfigScreen {
    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable(LanguageKey.CONFIG_TITLE))
                .setSavingRunnable(NarakaConfig::saveAll)
                .transparentBackground();

        createCommonCategory(builder);
        createClientCategory(builder);
        return builder.build();
    }

    private static void createCommonCategory(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory common = builder.getOrCreateCategory(Component.translatable(LanguageKey.CONFIG_CATEGORY_COMMON));

        common.addEntry(ConfigHelper.entry(NarakaConfig.COMMON.showTestCreativeModeTab, entryBuilder::startBooleanToggle)
                        .requireRestart()
                        .build())
                .addEntry(ConfigHelper.entry(NarakaConfig.COMMON.enableStigma, entryBuilder::startBooleanToggle).build())
                .addEntry(ConfigHelper.entry(NarakaConfig.COMMON.stigmaStunDuration, entryBuilder::startIntField).build())
                .addEntry(ConfigHelper.entry(NarakaConfig.COMMON.lockHealthRatio, entryBuilder::startFloatField).build());
    }

    private static void createClientCategory(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory client = builder.getOrCreateCategory(Component.translatable(LanguageKey.CONFIG_CATEGORY_CLIENT));

        client.addEntry(ConfigHelper.entry(NarakaConfig.CLIENT.playHerobrineBossMusic, entryBuilder::startBooleanToggle).build())
                .addEntry(ConfigHelper.entry(NarakaConfig.CLIENT.enableOreSeeThrough, entryBuilder::startBooleanToggle).build())
                .addEntry(ConfigHelper.entry(NarakaConfig.CLIENT.oreSeeThroughRange, entryBuilder::startIntField).build())
                .addEntry(ConfigHelper.entry(NarakaConfig.CLIENT.cameraShakingSpeed, entryBuilder::startFloatField).build())
                .addEntry(ConfigHelper.entry(NarakaConfig.CLIENT.cameraShakingStrength, entryBuilder::startFloatField).build())
                .addEntry(createOreColor(entryBuilder));
    }

    private static StringListListEntry createOreColor(ConfigEntryBuilder entryBuilder) {
        List<String> values = configToString(NarakaConfig.ORE_COLORS.values());
        List<String> defaultValues = configToString(NarakaConfig.ORE_COLORS.defaultValues());
        return entryBuilder.startStrList(Component.translatable(LanguageKey.CONFIG_ORE_COLOR), values)
                .setDefaultValue(defaultValues)
                .setSaveConsumer(NarakaConfigScreen::saveValues)
                .setCellErrorSupplier(NarakaConfigScreen::verifyColorConfig)
                .build();
    }

    private static List<String> configToString(Collection<Configuration.ConfigValue<Color>> values) {
        return values.stream()
                .map(value -> value.getKey() + "=" + value.getValue())
                .toList();
    }

    private static void saveValues(List<String> colors) {
        List<Configuration.ConfigValue<Color>> values = colors.stream()
                .map(NarakaConfigScreen::toConfigValue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        NarakaConfig.ORE_COLORS.loadValues(values);
    }

    private static Optional<Configuration.ConfigValue<Color>> toConfigValue(String text) {
        String[] array = text.split("=");
        if (array.length != 2)
            return Optional.empty();
        return Optional.of(new Configuration.ConfigValue<>(array[0], Color.of(array[1])));
    }

    private static Optional<Component> verifyColorConfig(String text) {
        if (text.matches("^(#?[a-z0-9_]+:[a-z0-9_]+|default_color)=#[0-9a-f]{8}$"))
            return Optional.empty();
        return Optional.of(Component.translatable(LanguageKey.CONFIG_ORE_COLOR_WRONG));
    }
}
