package com.yummy.naraka.config;

import java.io.IOException;
import java.io.Writer;

public interface ConfigWriter {
    <T> void write(Writer writer, String key, NarakaConfig.ConfigValue<T> value) throws IOException;
}
