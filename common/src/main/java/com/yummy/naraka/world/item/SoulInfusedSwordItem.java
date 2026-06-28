package com.yummy.naraka.world.item;

import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class SoulInfusedSwordItem extends SwordItem {
    private final int color;

    public SoulInfusedSwordItem(Tier tier, Item.Properties properties, int color) {
        super(tier, 5, -2.4f, properties);
        this.color = color;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return super.getName(itemStack)
                .copy()
                .withStyle(style -> style.withColor(color));
    }

    @Override
    public ItemStack getDefaultInstance() {
        return NarakaItemUtils.makeUnbreakable(super.getDefaultInstance());
    }
}
