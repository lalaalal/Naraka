package com.yummy.naraka.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SoulInfusedSwordItem extends Item {
    private final int color;

    public SoulInfusedSwordItem(Item.Properties properties, int color) {
        super(properties);
        this.color = color;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return super.getName(itemStack)
                .copy()
                .withColor(color);
    }
}
