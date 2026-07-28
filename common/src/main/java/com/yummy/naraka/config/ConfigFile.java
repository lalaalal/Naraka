package com.yummy.naraka.config;

import com.yummy.naraka.Platform;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Provides information of a configuration file.
 * Read and write {@link Configuration.ConfigValue}.
 */
public abstract class ConfigFile implements ConfigReader, ConfigWriter {
    public static final Path CONFIG_PATH = Platform.getInstance().getConfigurationPath();

    protected final File configFile;
    protected final File tmpFile;

    private final String configName;

    public ConfigFile(String configName) {
        this.configName = configName;
        this.configFile = CONFIG_PATH.resolve(configName + "." + getExtensionName()).toFile();
        this.tmpFile = CONFIG_PATH.resolve(configName + "." + getExtensionName() + ".tmp").toFile();
    }

    public abstract String getExtensionName();

    public boolean exists() {
        return configFile.exists();
    }

    /**
     * Check if the key exists without loading.
     *
     * @param key Key to check
     * @return True if the key exists
     */
    public abstract boolean contains(String key);

    public boolean contains(Configuration.ConfigValue<?> value) {
        return contains(value.getKey());
    }

    public String getFileName() {
        return configFile.getName();
    }

    public String getConfigName() {
        return configName;
    }

    public String getAbsolutePath() {
        return configFile.getAbsolutePath();
    }

    protected Reader createReader() throws IOException {
        return new BufferedReader(new FileReader(configFile));
    }

    protected Writer createWriter() throws IOException {
        return new BufferedWriter(new FileWriter(tmpFile));
    }

    public abstract void prepareWrite();

    private void finishWrite() throws IOException {
        if (tmpFile.exists())
            Files.move(tmpFile.toPath(), configFile.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public final void commit() throws IOException {
        try (Writer writer = createWriter()) {
            write(writer);
            writer.flush();
        }
        finishWrite();
    }

    abstract protected void write(Writer writer) throws IOException;
}
