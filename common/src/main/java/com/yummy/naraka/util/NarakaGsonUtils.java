package com.yummy.naraka.util;

import com.google.gson.*;

import java.lang.reflect.Type;

public class NarakaGsonUtils {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Color.class, new ColorAdapter())
            .create();

    private static class ColorAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
        @Override
        public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Color.of(json.getAsString());
        }

        @Override
        public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.toString(), String.class);
        }
    }
}
