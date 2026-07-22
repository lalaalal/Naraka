package com.yummy.naraka.world.item;

import com.yummy.naraka.event.ItemEvents;
import net.minecraft.world.item.Item;

public interface ItemDetailBuilder extends ItemDetailProvider {
    ItemDetailBuilder naraka$setItemTooltip(ItemEvents.ItemTooltip itemDetail);

    default ItemDetailBuilder naraka$setItemTooltip(NarakaItemTooltip itemTooltip) {
        return naraka$setItemTooltip(itemTooltip.tooltip());
    }

    Item.Properties naraka$asItemProperties();
}
