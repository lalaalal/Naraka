package com.yummy.naraka.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.NarakaGsonUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Set;

public class JsonConfigFile extends ConfigFile {
    private JsonObject cache = new JsonObject();

    public JsonConfigFile(String configFileName) {
        super(configFileName);
    }

    @Override
    public String getExtensionName() {
        return "json";
    }

    @Override
    public Reader createReader() throws IOException {
        this.cache = new JsonObject();
        return super.createReader();
    }

    @Override
    public Writer createWriter() throws IOException {
        this.cache = new JsonObject();
        return super.createWriter();
    }

    @Override
    public Set<String> getKeySet() {
        return cache.keySet();
    }

    @Override
    public Set<String> load(Reader reader) {
        checkReader(reader);
        this.cache = readJsonObject(reader);
        return cache.keySet();
    }

    private JsonObject readJsonObject(Reader reader) {
        try {
            JsonObject result = NarakaGsonUtils.GSON.fromJson(reader, JsonObject.class);
            if (result == null)
                return new JsonObject();
            return result;
        } catch (JsonIOException exception) {
            NarakaMod.LOGGER.error("An error occurred while reading config \"{}\"", getConfigName());
            NarakaMod.LOGGER.error(exception.getMessage());
        } catch (JsonSyntaxException exception) {
            NarakaMod.LOGGER.error("Json syntax error found in \"{}\"", getConfigName());
            NarakaMod.LOGGER.error(exception.getMessage());
            NarakaMod.LOGGER.warn("Ignore all config values in \"{}\"", getConfigName());
        }
        return new JsonObject();
    }

    @Override
    public boolean contains(String key) {
        return cache.has(key);
    }

    @Override
    public <T> void read(String key, Configuration.ConfigValue<T> configValue) {
        JsonElement element = cache.get(key);
        T value = NarakaGsonUtils.GSON.fromJson(element, configValue.getType());
        configValue.set(value);
    }

    @Override
    public <T> void write(Writer writer, String key, Configuration.ConfigValue<T> value) {
        JsonElement element = NarakaGsonUtils.GSON.toJsonTree(value.getValue());
        cache.add(key, element);
    }

    @Override
    public void commit(Writer writer) throws IOException {
        checkWriter(writer);
        NarakaGsonUtils.GSON.toJson(cache, writer);
        writer.flush();
    }
}
