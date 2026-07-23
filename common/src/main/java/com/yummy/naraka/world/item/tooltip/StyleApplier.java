package com.yummy.naraka.world.item.tooltip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.util.NarakaExtraCodecs;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringRepresentable;

public interface StyleApplier {
    Codec<StyleApplier> CODEC = Type.CODEC.dispatch("type", StyleApplier::type, Type::getValueCodec);

    MutableComponent apply(MutableComponent component);

    Type type();

    record Static(Style style) implements StyleApplier {
        public static final Codec<Static> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        NarakaExtraCodecs.STYLE.fieldOf("style").forGetter(Static::style)
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
        public static final Codec<Rainbow> CODEC = Codec.unit(INSTANCE);

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
        STATIC(Static.CODEC),
        RAINBOW(Rainbow.CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final Codec<? extends StyleApplier> valueCodec;

        Type(Codec<? extends StyleApplier> valueCodec) {
            this.valueCodec = valueCodec;
        }

        @Override
        public String getSerializedName() {
            return name();
        }

        public Codec<? extends StyleApplier> getValueCodec() {
            return valueCodec;
        }
    }
}
