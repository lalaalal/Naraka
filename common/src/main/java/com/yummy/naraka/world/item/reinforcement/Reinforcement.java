package com.yummy.naraka.world.item.reinforcement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Reinforcement for item
 *
 * @param value   Actual reinforcement value
 * @param effects Reinforcement effect holders
 * @see NarakaItemUtils
 */
public record Reinforcement(int value, HolderSet<ReinforcementEffect> effects) implements TooltipProvider {
    public static final Reinforcement ZERO = new Reinforcement(0, HolderSet.empty());
    public static final int MAX_VALUE = 10;

    private static final Component HEADER = Component.literal("@ ").withStyle(ChatFormatting.GRAY);

    public static final Codec<Reinforcement> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("value").forGetter(Reinforcement::value),
                    RegistryCodecs.homogeneousList(NarakaRegistries.Keys.REINFORCEMENT_EFFECT)
                            .fieldOf("effects")
                            .forGetter(Reinforcement::effects)
            ).apply(instance, Reinforcement::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Reinforcement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            Reinforcement::value,
            ByteBufCodecs.holderSet(NarakaRegistries.Keys.REINFORCEMENT_EFFECT),
            Reinforcement::effects,
            Reinforcement::new
    );

    public static Reinforcement zero(HolderSet<ReinforcementEffect> effects) {
        return new Reinforcement(0, effects);
    }

    public static Reinforcement zero(Holder<ReinforcementEffect> effect) {
        return new Reinforcement(0, HolderSet.direct(effect));
    }

    public static Reinforcement get(DataComponentHolder itemStack) {
        return itemStack.getOrDefault(NarakaDataComponentTypes.REINFORCEMENT.get(), ZERO);
    }

    public static boolean canReinforce(ItemStack itemStack) {
        if (itemStack.isEmpty() || itemStack.isStackable())
            return false;
        return get(itemStack).value < MAX_VALUE;
    }

    /**
     * Increase reinforcement of given item<br>
     * If item doesn't have reinforcement, given effects will be applied
     *
     * @param itemStack Item to increase reinforcement
     * @param effects   Applying effects if item doesn't have reinforcement
     * @return True if succeeded, false if value is bigger than {@linkplain #MAX_VALUE}
     * @see Reinforcement#increase(ItemStack, Holder)
     */
    public static boolean increase(ItemStack itemStack, HolderSet<ReinforcementEffect> effects) {
        Reinforcement original = itemStack.getOrDefault(NarakaDataComponentTypes.REINFORCEMENT.get(), zero(effects));
        if (original.value >= MAX_VALUE)
            return false;

        Reinforcement increased = original.increase();
        itemStack.set(NarakaDataComponentTypes.REINFORCEMENT.get(), increased);
        for (Holder<ReinforcementEffect> effect : effects)
            effect.value().onReinforcementIncreased(itemStack, original.value, increased.value);
        return true;
    }

    public static boolean increase(ItemStack itemStack, Holder<ReinforcementEffect> effect) {
        return increase(itemStack, HolderSet.direct(effect));
    }

    /**
     * Use {@link Reinforcement#increase(ItemStack, HolderSet)} to update effects
     *
     * @return Increased reinforcement
     */
    public Reinforcement increase() {
        return new Reinforcement(value + 1, effects);
    }

    public boolean canApplyEffect(Holder<ReinforcementEffect> effect, LivingEntity entity, EquipmentSlot slot, ItemStack itemStack) {
        return effects.contains(effect) && effect.value().canApply(entity, slot, itemStack, MAX_VALUE);
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> appender, TooltipFlag tooltipFlag) {
        if (value > 0) {
            Component reinforcementComponent = Component.translatable(LanguageKey.REINFORCEMENT_KEY, value)
                    .withStyle(ChatFormatting.YELLOW);
            if (NarakaConfig.CLIENT.showReinforcementValue.getValue())
                appender.accept(reinforcementComponent);

            for (Holder<ReinforcementEffect> effect : effects) {
                if (effect.value().showInTooltip(value)) {
                    String key = LanguageKey.reinforcementEffect(effect);
                    Component effectComponent = Component.translatable(key)
                            .withStyle(ChatFormatting.GRAY);
                    appender.accept(HEADER.copy().append(effectComponent));
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reinforcement(int value1, HolderSet<ReinforcementEffect> effects1))) return false;
        return value == value1 && Objects.equals(effects, effects1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, effects);
    }
}
