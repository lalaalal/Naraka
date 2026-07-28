package com.yummy.naraka.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import com.yummy.naraka.util.NarakaGsonUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Objects;
import java.util.Set;

public class JsonConfigFile extends ConfigFile {
    private static final Logger LOG = LogUtils.getLogger();

    private JsonObject buffer = new JsonObject();

    public JsonConfigFile(String configFileName) {
        super(configFileName);
    }

    @Override
    public String getExtensionName() {
        return "json";
    }

    @Override
    protected Reader createReader() throws IOException {
        this.buffer = new JsonObject();
        return super.createReader();
    }

    @Override
    public void prepareWrite() {
        this.buffer = new JsonObject();
    }

    @Override
    public Set<String> load() throws IOException {
        try (Reader reader = createReader()) {
            this.buffer = readJsonObject(reader);
            return buffer.keySet();
        }
    }

    private JsonObject readJsonObject(Reader reader) {
        try {
            return Objects.requireNonNullElse(NarakaGsonUtils.GSON.fromJson(reader, JsonObject.class), new JsonObject());
        } catch (JsonIOException exception) {
            LOG.error("An error occurred while reading config \"{}\"", getConfigName());
            LOG.error(exception.getMessage());
        } catch (JsonSyntaxException exception) {
            LOG.error("Json syntax error found in \"{}\"", getConfigName());
            LOG.error(exception.getMessage());
            LOG.warn("Ignore all config values in \"{}\"", getConfigName());
        }
        return new JsonObject();
    }

    @Override
    public boolean contains(String key) {
        return buffer.has(key);
    }

    @Override
    public <T> void read(String key, Configuration.ConfigValue<T> configValue) {
        JsonElement element = buffer.get(key);
        T value = NarakaGsonUtils.GSON.fromJson(element, configValue.getType());
        configValue.set(value);
    }

    @Override
    public <T> void appendToBuffer(String key, Configuration.ConfigValue<T> value) {
        JsonElement element = NarakaGsonUtils.GSON.toJsonTree(value.getValue());
        buffer.add(key, element);
    }

    @Override
    protected void write(Writer writer) {
        NarakaGsonUtils.GSON.toJson(buffer, writer);
    }
}
