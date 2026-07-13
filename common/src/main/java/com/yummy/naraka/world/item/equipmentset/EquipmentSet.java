package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.event.ItemEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class EquipmentSet implements ItemEvents.ItemTooltip {
    public static final Codec<HolderSet<EquipmentSet>> CODEC = RegistryCodecs.homogeneousList(NarakaRegistries.Keys.EQUIPMENT_SET_EFFECT_TYPE);
    public static final StreamCodec<RegistryFriendlyByteBuf, HolderSet<EquipmentSet>> STREAM_CODEC = ByteBufCodecs.holderSet(NarakaRegistries.Keys.EQUIPMENT_SET_EFFECT_TYPE);

    public static final EquipmentSet EMPTY = new EquipmentSet(List.of(), EquipmentSetEffect.EMPTY);

    private final List<Requirement> requirements;
    private final EquipmentSetEffect<?> effect;

    public EquipmentSet(List<Requirement> requirements, EquipmentSetEffect<?> effect) {
        this.requirements = requirements;
        this.effect = effect;
    }

    public boolean canApply(LivingEntity entity) {
        return requirements.stream().allMatch(requirement -> requirement.test(entity));
    }

    public void updateEffect(LivingEntity entity) {
        if (canApply(entity))
            effect.activate(entity);
        else effect.deactivate(entity);
    }

    @Override
    public void addToTooltip(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        Component component = Component.translatable(LanguageKey.EQUIPMENT_SET_KEY)
                .withStyle(styleUpdaterByEquipment(player));
        builder.accept(component);
    }

    private UnaryOperator<Style> styleUpdaterByEquipment(LivingEntity livingEntity) {
        if (canApply(livingEntity))
            return style -> style.withColor(ChatFormatting.GREEN);
        return style -> style.withStrikethrough(true)
                .withColor(ChatFormatting.GRAY);
    }

    public record Requirement(Holder<Item> item, EquipmentSlot slot,
                              DataComponentPatch components) implements Predicate<LivingEntity> {
        public static final Codec<Requirement> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(Requirement::item),
                        EquipmentSlot.CODEC.fieldOf("slot").forGetter(Requirement::slot),
                        DataComponentPatch.CODEC.fieldOf("components").forGetter(Requirement::components)
                ).apply(instance, Requirement::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Requirement> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(Registries.ITEM),
                Requirement::item,
                EquipmentSlot.STREAM_CODEC,
                Requirement::slot,
                DataComponentPatch.STREAM_CODEC,
                Requirement::components,
                Requirement::new
        );

        @Override
        public boolean test(LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            return itemStack.is(item.value()) && components.entrySet().stream().allMatch(entry -> {
                DataComponentType<?> type = entry.getKey();
                return entry.getValue()
                        .filter(value -> Objects.equals(itemStack.get(type), value))
                        .isPresent();
            });
        }
    }
}
