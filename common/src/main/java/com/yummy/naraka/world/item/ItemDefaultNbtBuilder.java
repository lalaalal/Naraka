package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.tooltip.DynamicItemLore;
import com.yummy.naraka.world.item.tooltip.DynamicItemLoreHolder;
import net.minecraft.world.item.Item;

public interface ItemDefaultNbtBuilder extends ItemDefaultNbtProvider {
    <T> ItemDefaultNbtBuilder naraka$set(String key, Codec<T> codec, T value);

    default ItemDefaultNbtBuilder naraka$withTooltip(DynamicItemLoreHolder dynamicItemLoreHolder) {
        return naraka$set(NarakaItemUtils.TAG_DYNAMIC_ITEM_LORE, DynamicItemLore.CODEC, dynamicItemLoreHolder.tooltip());
    }

    Item.Properties naraka$asItemProperties();
}
