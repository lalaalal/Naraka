package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.Platform;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class NarakaConfig {
    private static @Nullable WatchService watchService;

    private static final Set<Configuration> configurations = new HashSet<>();

    public static final NarakaCommonConfig COMMON = register(NarakaCommonConfig::new);
    public static final NarakaClientConfig CLIENT = registerForClient(NarakaClientConfig::new);
    public static final OreOutlineColorConfiguration ORE_COLORS = registerForClient(OreOutlineColorConfiguration::new);

    private static <T extends Configuration> T register(Supplier<T> provider) {
        T configuration = provider.get();
        configuration.loadValues();

        configurations.add(configuration);
        return configuration;
    }

    private static <T extends Configuration> T registerForClient(Supplier<T> provider) {
        T configuration = provider.get();
        if (Platform.getInstance().getSide() == Platform.Side.CLIENT) {
            configuration.loadValues();
            configurations.add(configuration);
        }
        return configuration;
    }

    public static void initialize() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            ConfigFile.CONFIG_PATH.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            Thread thread = new Thread(NarakaConfig::updateOnConfigChanges);
            thread.setName("Config Watcher");
            thread.start();
        } catch (IOException e) {
            NarakaMod.LOGGER.warn("Cannot watch config directory ({})", ConfigFile.CONFIG_PATH);
        }
    }

    private static void updateOnConfigChanges() {
        if (watchService == null)
            return;
        try {
            WatchKey watchKey;
            while ((watchKey = watchService.take()) != null) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    String changedFileName = event.context().toString();
                    configurations.stream()
                            .filter(configuration -> configuration.canUpdateOnFileChange(changedFileName))
                            .findAny()
                            .ifPresent(configuration -> {
                                NarakaMod.LOGGER.info("Configuration change detected \"{}\"", configuration.name);
                                configuration.loadValues();
                            });
                }
                watchKey.reset();
            }
        } catch (ClosedWatchServiceException | InterruptedException ignored) {

        }
    }

    public static void stopWatching() {
        try {
            if (watchService != null)
                watchService.close();
        } catch (IOException ignored) {

        }
    }
}
