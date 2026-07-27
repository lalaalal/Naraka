package com.yummy.naraka.config;

import com.yummy.naraka.Platform;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

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
        return new BufferedReader(new FileReader(configFile));
    }

    public Writer createWriter() throws IOException {
        return new BufferedWriter(new FileWriter(tmpFile));
    }

    private void finishWrite() throws IOException {
        if (tmpFile.exists())
            Files.move(tmpFile.toPath(), configFile.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public final void commit(Writer writer) throws IOException {
        write(writer);
        writer.flush();
        finishWrite();
    }

    abstract protected void write(Writer writer) throws IOException;
}
