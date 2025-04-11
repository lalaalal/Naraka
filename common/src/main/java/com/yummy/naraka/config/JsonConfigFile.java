package com.yummy.naraka.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.yummy.naraka.util.NarakaGsonUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Set;

public class JsonConfigFile extends ConfigFile {
    private static final JsonObject EMPTY = new JsonObject();
    private JsonObject cache = EMPTY;
    @Nullable
    private JsonWriter jsonWriter;

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
        Writer writer = super.createWriter();
        this.jsonWriter = NarakaGsonUtils.GSON.newJsonWriter(writer);
        this.jsonWriter.beginObject();
        return writer;
    }

    @Override
    public Set<String> load(Reader reader) throws IOException {
        if (cache.isEmpty()) {
            reader.reset();
            cache = NarakaGsonUtils.GSON.fromJson(reader, JsonObject.class);
        }
        if (cache == null)
            cache = EMPTY;
        return cache.keySet();
    }

    @Override
    public boolean contains(Reader reader, String key) throws IOException {
        load(reader);
        return cache.has(key);
    }

    @Override
    public <T> void read(Reader reader, String key, Configuration.ConfigValue<T> configValue) throws IOException {
        load(reader);
        JsonElement element = cache.get(key);
        T value = NarakaGsonUtils.GSON.fromJson(element, configValue.getType());
        configValue.set(value);
    }

    @Override
    public <T> void write(Writer writer, String key, Configuration.ConfigValue<T> value) throws IOException {
        if (jsonWriter == null)
            return;
        JsonElement element = NarakaGsonUtils.GSON.toJsonTree(value.getValue());
        jsonWriter.name(key);
        NarakaGsonUtils.GSON.toJson(element, jsonWriter);
    }

    @Override
    public void commit(Writer writer) throws IOException {
        if (jsonWriter == null)
            return;
        jsonWriter.endObject();
        jsonWriter.flush();
    }
}
