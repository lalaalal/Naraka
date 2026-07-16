package com.yummy.naraka.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class ItemEvents {
    public static final Event<ItemTooltip> ITEM_TOOLTIP_TOP = itemTooltip();
    public static final Event<ItemTooltip> ITEM_TOOLTIP_BOTTOM = itemTooltip();

    private static Event<ItemTooltip> itemTooltip() {
        return Event.create(listeners -> (itemStack, player, flag, builder) -> {
            for (ItemTooltip listener : listeners)
                listener.addToTooltip(itemStack, player, flag, builder);
        });
    }

    @FunctionalInterface
    public interface ItemTooltip {
        void addToTooltip(ItemStack itemStack, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder);
    }
}
