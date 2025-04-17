package com.yummy.naraka.config;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * Interface for loading and reading from a configuration file.
 */
public interface ConfigReader {
    /**
     * Load values from a given writer.
     *
     * @param reader File reader
     * @return Returns a set of keys
     * @throws IOException If an I/O exception occurs
     */
    Set<String> load(Reader reader) throws IOException;

    /**
     * Set value corresponding to the give key for {@link Configuration.ConfigValue}.
     * {@link ConfigReader#load(Reader)} <i>SHOULD</i> be called before this.
     *
     * @param key   Key of configuration
     * @param value Configuration value instance to set value
     * @param <T>   Type of value
     */
    <T> void read(String key, StaticConfiguration.ConfigValue<T> value);
}
