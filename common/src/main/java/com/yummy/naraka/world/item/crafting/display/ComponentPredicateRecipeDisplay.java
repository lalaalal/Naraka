package com.yummy.naraka.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public record ComponentPredicateRecipeDisplay(List<SlotDisplay> ingredients,
                                              SlotDisplay result) implements RecipeDisplay {
    public static final MapCodec<ComponentPredicateRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    SlotDisplay.CODEC.listOf().fieldOf("ingredients")
                            .forGetter(ComponentPredicateRecipeDisplay::ingredients),
                    SlotDisplay.CODEC.fieldOf("result")
                            .forGetter(ComponentPredicateRecipeDisplay::result)
            ).apply(instance, ComponentPredicateRecipeDisplay::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentPredicateRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ComponentPredicateRecipeDisplay::ingredients,
            SlotDisplay.STREAM_CODEC,
            ComponentPredicateRecipeDisplay::result,
            ComponentPredicateRecipeDisplay::new
    );

    public static final Type<ComponentPredicateRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    private static final SlotDisplay CRAFTING_STATION = new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE);

    @Override
    public SlotDisplay craftingStation() {
        return CRAFTING_STATION;
    }

    @Override
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}
