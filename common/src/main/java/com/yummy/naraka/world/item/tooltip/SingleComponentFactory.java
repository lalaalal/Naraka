package com.yummy.naraka.world.item.tooltip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record SingleComponentFactory(ComponentType componentType, String string,
                                     StyleApplier style) implements ComponentFactory {
    public static final Codec<SingleComponentFactory> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ComponentType.CODEC.fieldOf("type").forGetter(SingleComponentFactory::componentType),
                    Codec.STRING.fieldOf("string").forGetter(SingleComponentFactory::string),
                    StyleApplier.CODEC.fieldOf("style").forGetter(SingleComponentFactory::style)
            ).apply(instance, SingleComponentFactory::new)
    );

    public static final SingleComponentFactory EMPTY = SingleComponentFactory.literal("", StyleApplier.Static.EMPTY);

    public static SingleComponentFactory literal(String message, StyleApplier styleApplier) {
        return new SingleComponentFactory(ComponentType.LITERAL, message, styleApplier);
    }

    public static SingleComponentFactory translatable(String key, StyleApplier styleApplier) {
        return new SingleComponentFactory(ComponentType.TRANSLATABLE, key, styleApplier);
    }

    @Override
    public List<String> collectTranslationKeys() {
        List<String> translationKeys = new ArrayList<>();
        if (componentType == ComponentType.TRANSLATABLE)
            translationKeys.add(string);

        return translationKeys;
    }

    @Override
    public Component create() {
        MutableComponent component = componentType.create(string);
        style.apply(component);
        return component;
    }

    @Override
    public Type type() {
        return Type.SINGLE;
    }

    public enum ComponentType implements StringRepresentable {
        LITERAL(Component::literal),
        TRANSLATABLE(Component::translatable);

        public static final Codec<ComponentType> CODEC = StringRepresentable.fromEnum(ComponentType::values);

        private final Function<String, MutableComponent> constructor;

        ComponentType(Function<String, MutableComponent> constructor) {
            this.constructor = constructor;
        }

        @Override
        public String getSerializedName() {
            return name();
        }

        public MutableComponent create(String string) {
            return constructor.apply(string);
        }
    }
}
