package com.yummy.naraka.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Predicate;

public record ComponentPredicateIngredient(int row, int column, Ingredient ingredient,
                                           DataComponentPredicate.Single<?> predicate) implements Predicate<CraftingInput> {
    public static final Codec<ComponentPredicateIngredient> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 2).fieldOf("row").forGetter(ComponentPredicateIngredient::row),
                    Codec.intRange(0, 2).fieldOf("column").forGetter(ComponentPredicateIngredient::column),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(ComponentPredicateIngredient::ingredient),
                    DataComponentPredicate.singleCodec("predicate").forGetter(ComponentPredicateIngredient::predicate)
            ).apply(instance, ComponentPredicateIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::row,
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::column,
            Ingredient.CONTENTS_STREAM_CODEC,
            ComponentPredicateIngredient::ingredient,
            DataComponentPredicate.SINGLE_STREAM_CODEC,
            ComponentPredicateIngredient::predicate,
            ComponentPredicateIngredient::new
    );

    @Override
    public boolean test(CraftingInput input) {
        ItemStack itemStack = input.getItem(column, row);
        return ingredient.test(itemStack) && predicate.predicate().matches(itemStack);
    }
}
