package com.yummy.naraka.config;

import com.yummy.naraka.Platform;

import java.io.*;
import java.nio.file.Path;

public abstract class ConfigFile implements ConfigReader, ConfigWriter {
    public static final Path CONFIG_PATH = Platform.getInstance().getConfigurationPath();

    protected final File configFile;

    public ConfigFile(String configFileName) {
        this.configFile = CONFIG_PATH.resolve(configFileName + "." + getExtensionName()).toFile();
    }

    public abstract String getExtensionName();

    public abstract boolean contains(Reader reader, String key) throws IOException;

    public String getFileName() {
        return configFile.getName();
    }

    public Reader createReader() throws IOException {
        return new FileReader(configFile);
    }

    public Writer createWriter() throws IOException {
        return new FileWriter(configFile);
    }

    protected interface Parser<T, R> {
        R parse(T value);
    }
}
