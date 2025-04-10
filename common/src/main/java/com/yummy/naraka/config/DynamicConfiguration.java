package com.yummy.naraka.config;

import com.yummy.naraka.NarakaMod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class DynamicConfiguration<T> extends Configuration {
    protected final Map<String, ConfigValue<T>> configurations = new LinkedHashMap<>();
    private final Map<String, ConfigValue<T>> defaultValues = new LinkedHashMap<>();

    public DynamicConfiguration(String name, Function<String, ConfigFile> configFileFactory) {
        super(name, configFileFactory);
    }

    protected void addDefaultValue(String key, ConfigValue<T> defaultValue) {
        this.defaultValues.put(key, defaultValue);
    }

    protected abstract ConfigValue<T> createDefaultValue();

    @Override
    public boolean canUpdateOnFileChange(String fileName) {
        return file.getFileName().equals(fileName);
    }

    @Override
    public void loadValues() {
        configurations.clear();
        try (Reader reader = file.createReader()) {
            for (String key : file.load(reader)) {
                ConfigValue<T> value = createDefaultValue();
                configurations.put(key, value);
                file.read(reader, key, value);
            }
        } catch (FileNotFoundException exception) {
            saveValues();
            loadValues();
        } catch (IOException exception) {
            NarakaMod.LOGGER.error("An error occurred while loading config values");
        }
    }

    @Override
    public void saveValues() {
        try (Writer writer = file.createWriter()) {
            for (Map.Entry<String, ConfigValue<T>> entry : defaultValues.entrySet())
                file.write(writer, entry.getKey(), entry.getValue());
            file.commit(writer);
        } catch (IOException exception) {
            NarakaMod.LOGGER.error("An error occurred while saving default config values");
        }
    }
}
