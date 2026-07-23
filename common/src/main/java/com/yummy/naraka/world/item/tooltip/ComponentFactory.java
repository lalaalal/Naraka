package com.yummy.naraka.world.item.tooltip;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public interface ComponentFactory {
    Codec<ComponentFactory> CODEC = Type.CODEC.dispatch(ComponentFactory::type, Type::codec);

    Component create();

    List<String> collectTranslationKeys();

    Type type();

    enum Type implements StringRepresentable {
        SINGLE(SingleComponentFactory.CODEC),
        COMPOSITE(CompositeComponentFactory.CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final Codec<? extends ComponentFactory> mapCodec;

        Type(Codec<? extends ComponentFactory> mapCodec) {
            this.mapCodec = mapCodec;
        }

        public Codec<? extends ComponentFactory> codec() {
            return mapCodec;
        }

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}
