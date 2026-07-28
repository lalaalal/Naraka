package com.yummy.naraka.config;

import java.io.IOException;

/**
 * Interface for writing configuration to file.
 */
public interface ConfigWriter {
    /**
     * Write a single configuration.
     *
     * @param key    Key of configuration
     * @param value  Configuration value
     * @param <T>    Configuration type
     */
    <T> void appendToBuffer(String key, StaticConfiguration.ConfigValue<T> value);

    default <T> void appendToBuffer(Configuration.ConfigValue<T> value) {
        appendToBuffer(value.getKey(), value);
    }

    /**
     * Actually write to file.
     *
     * @throws IOException If an I/O exception occurs
     */
    void commit() throws IOException;
}
