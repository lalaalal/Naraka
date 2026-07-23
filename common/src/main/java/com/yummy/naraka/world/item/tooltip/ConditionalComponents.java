package com.yummy.naraka.world.item.tooltip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.item.NbtCondition;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record ConditionalComponents(NbtCondition condition,
                                    List<ComponentFactory> factories,
                                    boolean alwaysDisplay) implements ItemEvents.ItemTooltip {
    public static final Codec<ConditionalComponents> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    NbtCondition.CODEC.fieldOf("condition").forGetter(ConditionalComponents::condition),
                    ComponentFactory.CODEC.listOf().fieldOf("factories").forGetter(ConditionalComponents::factories),
                    Codec.BOOL.fieldOf("alwaysDisplay").forGetter(ConditionalComponents::alwaysDisplay)
            ).apply(instance, ConditionalComponents::new)
    );

    private static final Component HIDDEN = Component.translatable(LanguageKey.HIDDEN_TOOLTIP)
            .withStyle(ChatFormatting.DARK_GRAY);

    public static Builder any(ResourceLocation id) {
        return new Builder(id, NbtCondition.Type.ANY);
    }

    public static Builder all(ResourceLocation id) {
        return new Builder(id, NbtCondition.Type.ALL);
    }

    public boolean isAcceptable(ItemStack item) {
        return condition.test(item);
    }

    @Override
    public void addToTooltip(ItemStack item, Player player, TooltipFlag tooltipFlag, boolean shiftKeyPressed, Consumer<Component> builder) {
        if (shiftKeyPressed || alwaysDisplay) {
            for (ComponentFactory factory : factories)
                builder.accept(factory.create());
        } else {
            builder.accept(HIDDEN);
        }
    }

    public List<String> collectTranslationKeys() {
        List<String> translationKeys = new ArrayList<>();
        for (ComponentFactory factory : factories)
            translationKeys.addAll(factory.collectTranslationKeys());
        return translationKeys;
    }

    public static class Builder {
        private final ResourceLocation id;
        private NbtCondition.Type type;
        private int index;
        private final List<CompoundTag> conditions = new ArrayList<>();
        private final List<ComponentFactory> factories = new ArrayList<>();
        private CompositeComponentFactory current = CompositeComponentFactory.EMPTY;
        private boolean alwaysDisplay = true;

        public Builder(ResourceLocation id, NbtCondition.Type type) {
            this.id = id;
            this.type = type;
        }

        public Builder type(NbtCondition.Type type) {
            this.type = type;
            return this;
        }

        public Builder condition(CompoundTag condition) {
            conditions.add(condition);
            return this;
        }

        @SafeVarargs
        public final <T> Builder singleTypedConditions(String key, Codec<T> codec, T... values) {
            for (T value : values) {
                CompoundTag condition = new CompoundTag();
                NarakaNbtUtils.store(condition, key, codec, value);
                condition(condition);
            }
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

        public Builder alwaysDisplay(boolean alwaysDisplay) {
            this.alwaysDisplay = alwaysDisplay;
            return this;
        }

        public ConditionalComponents build() {
            return new ConditionalComponents(new NbtCondition(type, conditions), factories, alwaysDisplay);
        }
    }
}
