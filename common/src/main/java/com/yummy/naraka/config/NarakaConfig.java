package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;
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
    public static final NarakaClientConfig CLIENT = register(NarakaClientConfig::new);

    private static <T extends Configuration> T register(Supplier<T> provider) {
        T configuration = provider.get();
        configuration.loadValues();

        configurations.add(configuration);
        return configuration;
    }

    public static void initialize() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            ConfigFile.CONFIG_PATH.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            Thread thread = new Thread(NarakaConfig::updateOnConfigChanges);
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
                            .findFirst()
                            .ifPresent(Configuration::loadValues);
                }
                watchKey.reset();
            }
        } catch (ClosedWatchServiceException | InterruptedException ignored) {

        }
    }

    public static void stop() {
        try {
            if (watchService != null)
                watchService.close();
        } catch (IOException ignored) {

        }
    }
}
