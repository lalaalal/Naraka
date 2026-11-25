package com.yummy.naraka.network;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.yummy.naraka.util.NarakaExtraCodecs;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface CustomPacketPayload<T extends CustomPacketPayload<T>> {
    Type<T> type();
    
    record SimpleType<T extends CustomPacketPayload<T>>(ResourceLocation id, Class<T> classType,
                                                        BiConsumer<T, FriendlyByteBuf> encoder,
                                                        Function<FriendlyByteBuf, T> decoder) implements Type<T> {
        @Override
        public void encode(T payload, FriendlyByteBuf buffer) {
            encoder.accept(payload, buffer);
        }

        @Override
        public T decode(FriendlyByteBuf buffer) {
            return decoder.apply(buffer);
        }
    }

    record CodecType<T extends CustomPacketPayload<T>>(ResourceLocation id, Class<T> classType,
                                                       Codec<T> codec) implements Type<T> {
        @Override
        public void encode(T payload, FriendlyByteBuf buffer) {
            buffer.writeJsonWithCodec(codec, payload);
        }

        @Override
        public T decode(FriendlyByteBuf buffer) {
            return buffer.readJsonWithCodec(codec);
        }
    }

    class RegistryCodecType<T extends CustomPacketPayload<T>> implements Type<T> {
        private static final Set<RegistryCodecType<?>> REGISTRY_CODEC_TYPES = new HashSet<>();

        private final ResourceLocation id;
        private final Class<T> classType;
        private final Codec<T> codec;

        @Nullable
        private RegistryAccess registryAccess;

        public static void prepare(RegistryAccess registryAccess) {
            for (RegistryCodecType<?> registryCodecType : REGISTRY_CODEC_TYPES)
                registryCodecType.registryAccess = registryAccess;
        }

        public RegistryCodecType(ResourceLocation id, Class<T> classType, Codec<T> codec) {
            this.id = id;
            this.classType = classType;
            this.codec = codec;
            REGISTRY_CODEC_TYPES.add(this);
        }

        @Override
        public ResourceLocation id() {
            return id;
        }

        @Override
        public Class<T> classType() {
            return classType;
        }

        @Override
        public void encode(T payload, FriendlyByteBuf buffer) {
            if (registryAccess == null)
                throw new IllegalStateException("Registry is not prepared");
            DataResult<JsonElement> dataResult = codec.encodeStart(RegistryOps.create(JsonOps.INSTANCE, registryAccess), payload);
            buffer.writeUtf(NarakaExtraCodecs.GSON.toJson(Util.getOrThrow(dataResult, (string) -> new EncoderException("Failed to encode: " + string + " " + payload))));
        }

        @Override
        public T decode(FriendlyByteBuf buffer) {
            if (registryAccess == null)
                throw new IllegalStateException("Registry is not prepared");
            JsonElement jsonElement = GsonHelper.fromJson(NarakaExtraCodecs.GSON, buffer.readUtf(), JsonElement.class);
            DataResult<T> dataResult = codec.parse(JsonOps.INSTANCE, jsonElement);
            return Util.getOrThrow(dataResult, (string) -> new DecoderException("Failed to decode json: " + string));
        }
    }

    interface Type<T extends CustomPacketPayload<T>> {
        ResourceLocation id();

        Class<T> classType();

        void encode(T payload, FriendlyByteBuf buffer);

        T decode(FriendlyByteBuf buffer);
    }
}
