package com.yummy.naraka.world.item.tooltip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.util.ComponentStyles;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public interface StyleApplier {
    Codec<StyleApplier> CODEC = Type.CODEC.dispatch("type", StyleApplier::type, Type::getValueCodec);
    StreamCodec<ByteBuf, StyleApplier> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    MutableComponent apply(MutableComponent component);

    Type type();

    record Static(Style style) implements StyleApplier {
        public static final MapCodec<Static> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Style.Serializer.CODEC.fieldOf("style").forGetter(Static::style)
                ).apply(instance, Static::new)
        );

        public static final Static EMPTY = new Static(Style.EMPTY);

        @Override
        public MutableComponent apply(MutableComponent component) {
            return component.withStyle(style);
        }

        @Override
        public Type type() {
            return Type.STATIC;
        }
    }

    class Rainbow implements StyleApplier {
        public static final Rainbow INSTANCE = new Rainbow();
        public static final MapCodec<Rainbow> MAP_CODEC = MapCodec.unit(INSTANCE);

        private Rainbow() {

        }

        @Override
        public MutableComponent apply(MutableComponent component) {
            return component.withStyle(ComponentStyles.RAINBOW_COLOR);
        }

        @Override
        public Type type() {
            return Type.RAINBOW;
        }
    }

    enum Type implements StringRepresentable {
        STATIC(Static.MAP_CODEC),
        RAINBOW(Rainbow.MAP_CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromValues(Type::values);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        private final MapCodec<? extends StyleApplier> valueCodec;

        Type(MapCodec<? extends StyleApplier> valueCodec) {
            this.valueCodec = valueCodec;
        }

        @Override
        public String getSerializedName() {
            return name();
        }

        public MapCodec<? extends StyleApplier> getValueCodec() {
            return valueCodec;
        }
    }
}
