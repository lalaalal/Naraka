package com.yummy.naraka.config;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

public interface ConfigReader {
    Set<String> load(Reader reader) throws IOException;

    <T> void read(Reader reader, String key, StaticConfiguration.ConfigValue<T> value) throws IOException;
}
