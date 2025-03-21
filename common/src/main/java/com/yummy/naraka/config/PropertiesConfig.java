package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfig extends ConfigFile {
    private static final Map<Class<?>, Parser<String, ?>> PARSERS = Map.of(
            Boolean.class, Boolean::parseBoolean,
            Integer.class, Integer::parseInt,
            Long.class, Long::parseLong,
            Float.class, Float::parseFloat,
            Double.class, Double::parseDouble,
            String.class, value -> value
    );

    private final Properties cached = new Properties();

    public PropertiesConfig(String configFileName) {
        super(configFileName);
    }

    @Override
    public String getExtensionName() {
        return "properties";
    }

    @Override
    public Reader createReader() throws IOException {
        cached.clear();
        return super.createReader();
    }

    @Override
    public Writer createWriter() throws IOException {
        cached.clear();
        return super.createWriter();
    }

    @Override
    public boolean contains(Reader reader, String key) throws IOException {
        if (cached.isEmpty())
            this.cached.load(reader);
        return cached.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void read(Reader reader, String key, NarakaConfig.ConfigValue<T> value) throws IOException {
        if (cached.isEmpty())
            this.cached.load(reader);
        String property = cached.getProperty(key);
        Parser<String, T> parser = (Parser<String, T>) PARSERS.get(value.getType());
        if (property == null || parser == null) {
            NarakaMod.LOGGER.warn("Cannot load config value for key ({}), using default {}", key, value.getDefaultValue());
            return;
        }
        value.load(parser, property);
    }

    @Override
    public <T> void write(Writer writer, String key, NarakaConfig.ConfigValue<T> value) throws IOException {
        for (String comment : value.getComments())
            writer.write("# " + comment + "\n");
        writer.write("# default : " + value.getDefaultValue() + "\n");
        writer.write(key + "=" + value.getValue() + "\n");
    }
}
