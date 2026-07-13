package com.yummy.naraka.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;
import java.util.function.Predicate;

public record ComponentPredicateIngredient(int row, int column, Holder<Item> item, DataComponentPatch components)
        implements Predicate<CraftingInput> {
    public static final Codec<ComponentPredicateIngredient> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 2).fieldOf("row").forGetter(ComponentPredicateIngredient::row),
                    Codec.intRange(0, 2).fieldOf("column").forGetter(ComponentPredicateIngredient::column),
                    BuiltInRegistries.ITEM.holderByNameCodec()
                            .fieldOf("ingredient").forGetter(ComponentPredicateIngredient::item),
                    DataComponentPatch.CODEC.fieldOf("components").forGetter(ComponentPredicateIngredient::components)
            ).apply(instance, ComponentPredicateIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::row,
            ByteBufCodecs.VAR_INT,
            ComponentPredicateIngredient::column,
            ByteBufCodecs.holderRegistry(Registries.ITEM),
            ComponentPredicateIngredient::item,
            DataComponentPatch.STREAM_CODEC,
            ComponentPredicateIngredient::components,
            ComponentPredicateIngredient::new
    );

    public Ingredient componentAppliedIngredients() {
        return Ingredient.of(new ItemStack(item, 1, components));
    }

    @Override
    public boolean test(CraftingInput input) {
        ItemStack itemStack = input.getItem(column, row);
        return itemStack.is(item.value()) && components.entrySet().stream().allMatch(entry -> {
            DataComponentType<?> type = entry.getKey();
            return entry.getValue().filter(value -> Objects.equals(itemStack.get(type), value))
                    .isPresent();
        });
    }
}
