package com.yummy.naraka.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.component.DataComponentApplier;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.function.Predicate;

public record ComponentPredicateIngredient(int row, int column, Ingredient ingredient,
                                           DataComponentPredicate predicate,
                                           DataComponentApplier.Single<?> applier) implements Predicate<CraftingInput> {
    public static final Codec<ComponentPredicateIngredient> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 2).fieldOf("row").forGetter(ComponentPredicateIngredient::row),
                    Codec.intRange(0, 2).fieldOf("column").forGetter(ComponentPredicateIngredient::column),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(ComponentPredicateIngredient::ingredient),
                    DataComponentPredicate.CODEC.fieldOf("predicate").forGetter(ComponentPredicateIngredient::predicate),
                    DataComponentApplier.SINGLE_CODEC.fieldOf("applier").forGetter(ComponentPredicateIngredient::applier)
            ).apply(instance, ComponentPredicateIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::row,
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::column,
            Ingredient.CONTENTS_STREAM_CODEC,
            ComponentPredicateIngredient::ingredient,
            DataComponentPredicate.STREAM_CODEC,
            ComponentPredicateIngredient::predicate,
            DataComponentApplier.SINGLE_STREAM_CODEC,
            ComponentPredicateIngredient::applier,
            ComponentPredicateIngredient::new
    );

    public Ingredient componentAppliedIngredients() {
        return Ingredient.of(
                Arrays.stream(ingredient.getItems())
                        .map(applier::apply)
        );
    }

    @Override
    public boolean test(CraftingInput input) {
        ItemStack itemStack = input.getItem(column, row);
        return ingredient.test(itemStack) && predicate.test(itemStack);
    }
}
