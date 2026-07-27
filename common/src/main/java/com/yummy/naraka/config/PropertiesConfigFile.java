package com.yummy.naraka.config;

import com.mojang.logging.LogUtils;
import com.yummy.naraka.util.Color;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PropertiesConfigFile extends ConfigFile {
    private static final Logger LOG = LogUtils.getLogger();
    private static final Map<Class<?>, Function<String, ?>> PARSERS = Map.of(
            Boolean.class, Boolean::parseBoolean,
            Integer.class, Integer::parseInt,
            Long.class, Long::parseLong,
            Float.class, Float::parseFloat,
            Double.class, Double::parseDouble,
            String.class, Function.identity(),
            Color.class, Color::of
    );

    private final Properties cache = new Properties();
    private StringBuilder buffer = new StringBuilder();

    public PropertiesConfigFile(String configFileName) {
        super(configFileName);
    }

    @Override
    public String getExtensionName() {
        return "properties";
    }

    private Set<String> getKeySet() {
        return cache.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Override
    public Reader createReader() throws IOException {
        cache.clear();
        return super.createReader();
    }

    @Override
    public Writer createWriter() throws IOException {
        buffer = new StringBuilder();
        return super.createWriter();
    }

    @Override
    public Set<String> load(Reader reader) throws IOException {
        this.cache.load(reader);
        return getKeySet();
    }

    @Override
    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void read(String key, StaticConfiguration.ConfigValue<T> value) {
        String property = cache.getProperty(key);
        Function<String, T> parser = (Function<String, T>) PARSERS.get(value.getType());
        if (property == null || parser == null) {
            LOG.warn("Cannot load config value for key ({}), using default {}", key, value.getDefaultValue());
            return;
        }
        value.set(parser.apply(property));
    }

    @Override
    public <T> void appendToBuffer(String key, StaticConfiguration.ConfigValue<T> value) {
        for (String comment : value.getComments())
            buffer.append("# ").append(comment).append("\n");
        buffer.append("# default : ").append(value.getDefaultValue()).append("\n");
        buffer.append(key).append("=").append(value.getValue()).append("\n");
        buffer.append("\n");
    }

    @Override
    protected void write(Writer writer) throws IOException {
        writer.write(buffer.toString());
    }
}
