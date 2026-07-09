package com.yummy.naraka.world.item;

import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class SoulInfusedSwordItem extends SwordItem {
    private final SoulType type;

    public SoulInfusedSwordItem(Tier tier, Item.Properties properties, SoulType soulType) {
        super(tier, 5, -2.4f, properties);
        this.type = soulType;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return super.getName(itemStack)
                .copy()
                .withStyle(style -> style.withColor(type.color));
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = NarakaItemUtils.makeUnbreakable(super.getDefaultInstance());
        return NarakaItemUtils.storeNbtData(itemStack, NarakaItemUtils.TAG_SOUL_TYPE, SoulType.CODEC, type);
    }
}
