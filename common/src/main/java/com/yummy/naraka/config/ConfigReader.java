package com.yummy.naraka.config;

import java.io.IOException;
import java.io.Reader;

public interface ConfigReader {
    <T> void read(Reader reader, String key, Configuration.ConfigValue<T> value) throws IOException;
}
