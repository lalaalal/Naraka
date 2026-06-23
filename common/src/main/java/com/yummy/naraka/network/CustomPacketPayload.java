package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

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

    interface Type<T extends CustomPacketPayload<T>> {
        ResourceLocation id();

        Class<T> classType();

        void encode(T payload, FriendlyByteBuf buffer);

        T decode(FriendlyByteBuf buffer);
    }
}
