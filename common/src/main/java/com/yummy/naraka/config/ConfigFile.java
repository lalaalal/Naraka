package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.Platform;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.util.Set;

/**
 * Provides information of a configuration file.
 * Read and write {@link Configuration.ConfigValue}.
 */
public abstract class ConfigFile implements ConfigReader, ConfigWriter {
    public static final Path CONFIG_PATH = Platform.getInstance().getConfigurationPath();

    protected final File configFile;

    private final String configName;
    private @Nullable BufferedReader reader;
    private @Nullable BufferedWriter writer;

    public ConfigFile(String configName) {
        this.configName = configName;
        this.configFile = CONFIG_PATH.resolve(configName + "." + getExtensionName()).toFile();
    }

    public abstract String getExtensionName();

    /**
     * Returns key set without loading.
     *
     * @return Key set
     * @see ConfigReader#load(Reader)
     */
    public abstract Set<String> getKeySet();

    /**
     * Check if the key exists without loading.
     *
     * @param key Key to check
     * @return True if the key exists
     * @see ConfigReader#load(Reader)
     */
    public abstract boolean contains(String key);

    public String getFileName() {
        return configFile.getName();
    }

    public String getConfigName() {
        return configName;
    }

    public String getAbsolutePath() {
        return configFile.getAbsolutePath();
    }

    public Reader createReader() throws IOException {
        return this.reader = new BufferedReader(new FileReader(configFile));
    }

    public Writer createWriter() throws IOException {
        return this.writer = new BufferedWriter(new FileWriter(configFile));
    }

    protected void checkReader(Reader reader) {
        if (reader != this.reader)
            NarakaMod.LOGGER.warn("{} is using external reader", getConfigName());
    }

    protected void checkWriter(Writer writer) {
        if (writer != this.writer)
            NarakaMod.LOGGER.warn("{} is using external writer", getConfigName());
    }
}
