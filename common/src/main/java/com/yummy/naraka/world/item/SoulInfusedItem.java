package com.yummy.naraka.world.item;

import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SoulInfusedItem extends Item {
    private final SoulType defaultSoulType;

    public SoulInfusedItem(Properties properties, SoulType defaultSoulType) {
        super(properties);
        this.defaultSoulType = defaultSoulType;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return NarakaItemUtils.storeNbtData(super.getDefaultInstance(), "SoulType", SoulType.CODEC, defaultSoulType);
    }
}
