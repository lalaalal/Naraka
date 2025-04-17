package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.Color;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertiesConfigFile extends ConfigFile {
    private static final Map<Class<?>, Parser<String, ?>> PARSERS = Map.of(
            Boolean.class, Boolean::parseBoolean,
            Integer.class, Integer::parseInt,
            Long.class, Long::parseLong,
            Float.class, Float::parseFloat,
            Double.class, Double::parseDouble,
            String.class, value -> value,
            Color.class, Color::of
    );

    private final Properties cache = new Properties();

    public PropertiesConfigFile(String configFileName) {
        super(configFileName);
    }

    @Override
    public String getExtensionName() {
        return "properties";
    }

    @Override
    public Set<String> getKeySet() {
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
        return super.createWriter();
    }

    @Override
    public Set<String> load(Reader reader) throws IOException {
        checkReader(reader);
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
        Parser<String, T> parser = (Parser<String, T>) PARSERS.get(value.getType());
        if (property == null || parser == null) {
            NarakaMod.LOGGER.warn("Cannot load config value for key ({}), using default {}", key, value.getDefaultValue());
            return;
        }
        value.set(parser.parse(property));
    }

    @Override
    public <T> void write(Writer writer, String key, StaticConfiguration.ConfigValue<T> value) throws IOException {
        for (String comment : value.getComments())
            writer.write("# " + comment + "\n");
        writer.write("# default : " + value.getDefaultValue() + "\n");
        writer.write(key + "=" + value.getValue() + "\n");
    }

    @Override
    public void commit(Writer writer) throws IOException {
        checkWriter(writer);
        writer.flush();
    }
}
