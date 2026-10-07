package com.yummy.naraka.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.Objects;
import java.util.function.Predicate;

public record ComponentPredicateIngredient(int row, int column, HolderSet<Item> ingredient,
                                           DataComponentMap components) implements Predicate<CraftingInput> {
    public static final Codec<ComponentPredicateIngredient> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 2).fieldOf("row").forGetter(ComponentPredicateIngredient::row),
                    Codec.intRange(0, 2).fieldOf("column").forGetter(ComponentPredicateIngredient::column),
                    RegistryCodecs.holderSet(Registries.ITEM).fieldOf("ingredient").forGetter(ComponentPredicateIngredient::ingredient),
                    DataComponentMap.CODEC.fieldOf("components").forGetter(ComponentPredicateIngredient::components)
            ).apply(instance, ComponentPredicateIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::row,
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::column,
            ByteBufCodecs.holderSet(Registries.ITEM),
            ComponentPredicateIngredient::ingredient,
            ByteBufCodecs.fromCodecWithRegistries(DataComponentMap.CODEC),
            ComponentPredicateIngredient::components,
            ComponentPredicateIngredient::new
    );

    @Override
    public boolean test(CraftingInput input) {
        ItemStack itemStack = input.getItem(column, row);
        return itemStack.is(ingredient) && components.stream().allMatch(typedDataComponent -> {
            DataComponentType<?> type = typedDataComponent.type();
            return Objects.equals(itemStack.get(type), typedDataComponent.value());
        });
    }

    public SlotDisplay display() {
        return new SlotDisplay.Composite(ingredient.stream()
                .map(this::displayForSingleItem)
                .toList()
        );
    }

    private SlotDisplay displayForSingleItem(Holder<Item> item) {
        DataComponentPatch.Builder builder = DataComponentPatch.builder();
        components.forEach(builder::set);
        return new SlotDisplay.ItemStackSlotDisplay(new ItemStackTemplate(item, 1, builder.build()));
    }
}
