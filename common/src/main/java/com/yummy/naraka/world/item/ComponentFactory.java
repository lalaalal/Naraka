package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public interface ComponentFactory {
    Codec<ComponentFactory> CODEC = Type.CODEC.dispatch(ComponentFactory::type, Type::mapCodec);
    StreamCodec<ByteBuf, ComponentFactory> STREAM_CODEC = Type.STREAM_CODEC.dispatch(ComponentFactory::type, Type::streamCodec);

    Component create();

    List<String> collectTranslationKeys();

    Type type();

    enum Type implements StringRepresentable {
        SINGLE(SingleComponentFactory.CODEC, SingleComponentFactory.STREAM_CODEC),
        COMPOSITE(CompositeComponentFactory.CODEC, SingleComponentFactory.STREAM_CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromValues(Type::values);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        private final MapCodec<? extends ComponentFactory> mapCodec;
        private final StreamCodec<ByteBuf, ? extends ComponentFactory> streamCodec;

        Type(MapCodec<? extends ComponentFactory> mapCodec, StreamCodec<ByteBuf, ? extends ComponentFactory> streamCodec) {
            this.mapCodec = mapCodec;
            this.streamCodec = streamCodec;
        }

        public MapCodec<? extends ComponentFactory> mapCodec() {
            return mapCodec;
        }

        public StreamCodec<ByteBuf, ? extends ComponentFactory> streamCodec() {
            return streamCodec;
        }

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}
