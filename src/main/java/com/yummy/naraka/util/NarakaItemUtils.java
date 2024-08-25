package com.yummy.naraka.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class NarakaItemUtils {
    @SuppressWarnings("deprecation")
    public static ItemAttributeModifiers getAttributeModifiers(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, itemStack.getItem().getDefaultAttributeModifiers());
    }
}
