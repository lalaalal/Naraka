package com.yummy.naraka.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ToolMaterial;

public class SoulInfusedSwordItem extends SwordItem {
    private final int color;

    public SoulInfusedSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties, int color) {
        super(material, attackDamage, attackSpeed, properties);
        this.color = color;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return super.getName(itemStack)
                .copy()
                .withColor(color);
    }
}
