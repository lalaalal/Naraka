package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.event.ItemEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public record ConditionalComponents(ConditionType type, List<DataComponentPatch> conditions,
                                    List<ComponentFactory> factories) implements ItemEvents.ItemTooltip {
    public static final Codec<ConditionalComponents> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ConditionType.CODEC.fieldOf("type").forGetter(ConditionalComponents::type),
                    DataComponentPatch.CODEC.listOf().fieldOf("conditions").forGetter(ConditionalComponents::conditions),
                    ComponentFactory.CODEC.listOf().fieldOf("factories").forGetter(ConditionalComponents::factories)
            ).apply(instance, ConditionalComponents::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ConditionalComponents> STREAM_CODEC = StreamCodec.composite(
            ConditionType.STREAM_CODEC,
            ConditionalComponents::type,
            DataComponentPatch.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ConditionalComponents::conditions,
            ComponentFactory.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ConditionalComponents::factories,
            ConditionalComponents::new
    );

    public static Builder any(Identifier id) {
        return new Builder(id, ConditionType.ANY);
    }

    public static Builder all(Identifier id) {
        return new Builder(id, ConditionType.ALL);
    }

    public boolean isAcceptable(DataComponentHolder item) {
        return type.test(item, conditions);
    }

    @Override
    public void addToTooltip(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        for (ComponentFactory factory : factories)
            builder.accept(factory.create());
    }

    public List<String> collectTranslationKeys() {
        List<String> translationKeys = new ArrayList<>();
        for (ComponentFactory factory : factories)
            translationKeys.addAll(factory.collectTranslationKeys());
        return translationKeys;
    }

    public enum ConditionType implements StringRepresentable {
        ANY(ConditionType::any),
        ALL(ConditionType::all);

        public static final Codec<ConditionType> CODEC = StringRepresentable.fromValues(ConditionType::values);
        public static final StreamCodec<ByteBuf, ConditionType> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        private final BiPredicate<DataComponentHolder, List<DataComponentPatch>> predicate;

        ConditionType(BiPredicate<DataComponentHolder, List<DataComponentPatch>> predicate) {
            this.predicate = predicate;
        }

        private static boolean testSingle(DataComponentHolder item, DataComponentPatch condition) {
            for (Map.Entry<DataComponentType<?>, Optional<?>> entry : condition.entrySet()) {
                DataComponentType<?> type = entry.getKey();
                Optional<?> value = entry.getValue();
                if (value.isEmpty())
                    continue;
                if (!Objects.equals(item.get(type), value.get()))
                    return false;
            }
            return true;
        }

        private static boolean any(DataComponentHolder item, List<DataComponentPatch> conditions) {
            if (conditions.isEmpty())
                return true;
            for (DataComponentPatch condition : conditions) {
                if (testSingle(item, condition))
                    return true;
            }
            return false;
        }

        private static boolean all(DataComponentHolder item, List<DataComponentPatch> conditions) {
            for (DataComponentPatch condition : conditions) {
                if (!testSingle(item, condition))
                    return false;
            }
            return true;
        }

        public boolean test(DataComponentHolder item, List<DataComponentPatch> conditions) {
            return predicate.test(item, conditions);
        }

        @Override
        public String getSerializedName() {
            return name();
        }
    }

    public static class Builder {
        private final Identifier id;
        private ConditionType type;
        private int index;
        private final List<DataComponentPatch> conditions = new ArrayList<>();
        private final List<ComponentFactory> factories = new ArrayList<>();
        private CompositeComponentFactory current = CompositeComponentFactory.EMPTY;

        public Builder(Identifier id, ConditionType type) {
            this.id = id;
            this.type = type;
        }

        public Builder type(ConditionType type) {
            this.type = type;
            return this;
        }

        public Builder condition(DataComponentPatch.Builder condition) {
            conditions.add(condition.build());
            return this;
        }

        @SafeVarargs
        public final <T> Builder singleTypedConditions(DataComponentType<T> type, T... values) {
            for (T value : values)
                condition(DataComponentPatch.builder().set(type, value));
            return this;
        }

        public Builder append(SingleComponentFactory.ComponentType componentType, String string, StyleApplier style) {
            if (current == CompositeComponentFactory.EMPTY) {
                current = CompositeComponentFactory.of(new SingleComponentFactory(componentType, string, style));
            } else {
                current = current.append(new SingleComponentFactory(componentType, string, style));
            }
            return this;
        }

        public Builder appendLiteral(String text, StyleApplier style) {
            return append(SingleComponentFactory.ComponentType.LITERAL, text, style);
        }

        public Builder appendTranslatableWithSuffix(String suffix, StyleApplier style) {
            return append(SingleComponentFactory.ComponentType.TRANSLATABLE, LanguageKey.tooltip(id, suffix), style);
        }

        public Builder appendTranslatable(String key, StyleApplier style) {
            return append(SingleComponentFactory.ComponentType.TRANSLATABLE, key, style);
        }

        public Builder appendTranslatable(StyleApplier style) {
            String translationKey = LanguageKey.tooltip(id, String.valueOf(index));
            this.index += 1;
            return appendTranslatable(translationKey, style);
        }

        public Builder line(StyleApplier style) {
            String translationKey = LanguageKey.tooltip(id, String.valueOf(index));
            this.index += 1;
            this.factories.add(SingleComponentFactory.translatable(translationKey, style));
            return this;
        }

        public Builder newLine() {
            this.factories.add(current);
            this.current = CompositeComponentFactory.EMPTY;
            return this;
        }

        public ConditionalComponents build() {
            return new ConditionalComponents(type, conditions, factories);
        }
    }
}
