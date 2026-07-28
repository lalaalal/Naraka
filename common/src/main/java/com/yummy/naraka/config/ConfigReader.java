package com.yummy.naraka.config;

import java.io.IOException;
import java.util.Set;

/**
 * Interface for loading and reading from a configuration file.
 */
public interface ConfigReader {
    /**
     * Load values from a given writer.
     *
     * @return Returns a set of keys
     * @throws IOException If an I/O exception occurs
     */
    Set<String> load() throws IOException;

    /**
     * Set value corresponding to the give key for {@link Configuration.ConfigValue}.
     *
     * @param key   Key of configuration
     * @param value Configuration value instance to set value
     * @param <T>   Type of value
     */
    <T> void read(String key, StaticConfiguration.ConfigValue<T> value);

    default <T> void read(Configuration.ConfigValue<T> value) {
        read(value.getKey(), value);
    }
}
