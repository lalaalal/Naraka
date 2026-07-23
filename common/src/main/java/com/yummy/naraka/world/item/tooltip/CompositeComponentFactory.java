package com.yummy.naraka.world.item.tooltip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public record CompositeComponentFactory(List<SingleComponentFactory> children) implements ComponentFactory {
    public static final Codec<CompositeComponentFactory> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    SingleComponentFactory.CODEC.listOf().fieldOf("children").forGetter(CompositeComponentFactory::children)
            ).apply(instance, CompositeComponentFactory::new)
    );

    public static final CompositeComponentFactory EMPTY = new CompositeComponentFactory(List.of());

    public static CompositeComponentFactory of(SingleComponentFactory... children) {
        return new CompositeComponentFactory(List.of(children));
    }

    public CompositeComponentFactory append(SingleComponentFactory... children) {
        List<SingleComponentFactory> newChildren = new ArrayList<>(this.children);
        newChildren.addAll(List.of(children));
        return new CompositeComponentFactory(newChildren);
    }

    @Override
    public Component create() {
        MutableComponent component = Component.empty();
        for (SingleComponentFactory child : children)
            component.append(child.create());
        return component;
    }

    @Override
    public List<String> collectTranslationKeys() {
        List<String> translationKeys = new ArrayList<>();
        for (SingleComponentFactory child : children)
            translationKeys.addAll(child.collectTranslationKeys());
        return translationKeys;
    }

    @Override
    public Type type() {
        return Type.COMPOSITE;
    }
}
