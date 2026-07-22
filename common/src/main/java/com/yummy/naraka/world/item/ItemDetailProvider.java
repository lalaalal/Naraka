package com.yummy.naraka.world.item;

import com.yummy.naraka.event.ItemEvents;

import java.util.Optional;

public interface ItemDetailProvider {
    Optional<ItemEvents.ItemTooltip> naraka$getItemTooltip();
}
