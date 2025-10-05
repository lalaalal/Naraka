package com.yummy.naraka.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.component.ComponentItemApply;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.function.Predicate;

public record ComponentPredicateIngredient(int row, int column, HolderSet<Item> ingredient,
                                           DataComponentPredicate.Single<?> predicate) implements Predicate<CraftingInput> {
    public static final Codec<ComponentPredicateIngredient> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 2).fieldOf("row").forGetter(ComponentPredicateIngredient::row),
                    Codec.intRange(0, 2).fieldOf("column").forGetter(ComponentPredicateIngredient::column),
                    HolderSetCodec.create(Registries.ITEM, Item.CODEC, false).fieldOf("ingredient").forGetter(ComponentPredicateIngredient::ingredient),
                    DataComponentPredicate.singleCodec("predicate").forGetter(ComponentPredicateIngredient::predicate)
            ).apply(instance, ComponentPredicateIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::row,
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::column,
            ByteBufCodecs.holderSet(Registries.ITEM),
            ComponentPredicateIngredient::ingredient,
            DataComponentPredicate.SINGLE_STREAM_CODEC,
            ComponentPredicateIngredient::predicate,
            ComponentPredicateIngredient::new
    );

    @Override
    public boolean test(CraftingInput input) {
        ItemStack itemStack = input.getItem(column, row);
        return itemStack.is(ingredient) && predicate.predicate().matches(itemStack);
    }

    public SlotDisplay display() {
        return new SlotDisplay.Composite(ingredient.stream()
                .map(this::displayForSingleItem)
                .toList()
        );
    }

    private SlotDisplay displayForSingleItem(Holder<Item> item) {
        ItemStack itemStack = new ItemStack(item);
        if (predicate.predicate() instanceof ComponentItemApply componentItemApply) {
            componentItemApply.apply(itemStack);
        }
        return new SlotDisplay.ItemStackSlotDisplay(itemStack);
    }
}
