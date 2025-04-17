package com.yummy.naraka.config;

import java.io.IOException;
import java.io.Writer;

/**
 * Interface for writing configuration to file.
 */
public interface ConfigWriter {
    /**
     * Write a single configuration.
     *
     * @param writer Writer
     * @param key    Key of configuration
     * @param value  Configuration value
     * @param <T>    Configuration type
     * @throws IOException If an I/O exception occurs
     */
    <T> void write(Writer writer, String key, StaticConfiguration.ConfigValue<T> value) throws IOException;

    /**
     * Actually write to file.
     *
     * @param writer File writer
     * @throws IOException If an I/O exception occurs
     */
    void commit(Writer writer) throws IOException;
}
