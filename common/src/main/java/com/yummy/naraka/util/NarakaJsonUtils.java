package com.yummy.naraka.util;

import com.google.gson.*;
import com.yummy.naraka.NarakaConfig;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;

public class NarakaJsonUtils {
    private static final Map<Class<?>, Function<JsonElement, ?>> JSON_PARSERS = Map.of(
            Integer.class, JsonElement::getAsInt,
            Boolean.class, JsonElement::getAsBoolean,
            Double.class, JsonElement::getAsDouble,
            Float.class, JsonElement::getAsFloat
    );

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(NarakaConfig.ConfigValue.class, new ConfigValueDeserializer())
            .create();

    @SuppressWarnings("unchecked")
    public static <T> T parser(Class<T> type, JsonElement jsonElement) {
        if (JSON_PARSERS.containsKey(type))
            return (T) JSON_PARSERS.get(type).apply(jsonElement);
        throw new IllegalStateException("Cannot find matching json parser for " + type);
    }

    public static class ConfigValueDeserializer implements JsonSerializer<NarakaConfig.ConfigValue<?>> {
        @Override
        public JsonElement serialize(NarakaConfig.ConfigValue<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.getValue());
        }
    }
}
