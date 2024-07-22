package com.yummy.naraka.client;

import java.util.HashMap;
import java.util.Map;

import com.yummy.naraka.NarakaMod;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NarakaModels {
    private static final Map<Item, ModelResourceLocation> modelMappings = new HashMap<>();

    public static final ModelResourceLocation SPEAR_IN_HAND = inventory("spear_in_hand");
    public static final ModelResourceLocation MIGHTY_HOLY_SPEAR_IN_HAND = inventory("mighty_holy_spear_in_hand");
    public static final ModelResourceLocation SPEAR_OF_LONGINUS_IN_HAND = inventory("spear_of_longinus_in_hand");

    private static ModelResourceLocation inventory(String path) {
        return ModelResourceLocation.inventory(NarakaMod.location(path));
    }

    private static ModelResourceLocation location(String path) {
        return new ModelResourceLocation(NarakaMod.location(path), "");
    }

    public static ModelResourceLocation getInHandModel(ItemStack stack) {
        return modelMappings.get(stack.getItem());
    }
}
