package com.yummy.naraka.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

public class NarakaItemUtils {
    @SuppressWarnings("deprecation")
    public static ItemAttributeModifiers getAttributeModifiers(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, itemStack.getItem().getDefaultAttributeModifiers());
    }

    public static void addItemEntity(Level level, BlockPos pos, Item item, int count) {
        if (!level.isClientSide()) {
            ItemStack itemStack = new ItemStack(item, count);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack));
        }
    }
}
