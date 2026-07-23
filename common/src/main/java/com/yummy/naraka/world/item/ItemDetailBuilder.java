package com.yummy.naraka.world.item;

import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.world.item.tooltip.DynamicItemLoreHolder;
import net.minecraft.world.item.Item;

public interface ItemDetailBuilder extends ItemDetailProvider {
    ItemDetailBuilder naraka$setItemTooltip(ItemEvents.ItemTooltip itemDetail);

    default ItemDetailBuilder naraka$setItemTooltip(DynamicItemLoreHolder dynamicItemLoreHolder) {
        return naraka$setItemTooltip(dynamicItemLoreHolder.tooltip());
    }

    Item.Properties naraka$asItemProperties();
}
