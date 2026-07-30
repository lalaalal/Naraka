package com.yummy.naraka.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class ItemEvents {
    public static final Event<ItemTooltip> ITEM_TOOLTIP_TOP = itemTooltip();
    public static final Event<ItemTooltip> ITEM_TOOLTIP_MIDDLE = itemTooltip();
    public static final Event<ItemTooltip> ITEM_TOOLTIP_BOTTOM = itemTooltip();

    private static Event<ItemTooltip> itemTooltip() {
        return Event.create(listeners -> (item, context, player, flag, shiftKeyPressed, builder) -> {
            for (ItemTooltip listener : listeners)
                listener.addToTooltip(item, context, player, flag, shiftKeyPressed, builder);
        });
    }

    @FunctionalInterface
    public interface ItemTooltip {
        void addToTooltip(ItemStack item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, boolean shiftKeyPressed, Consumer<Component> builder);
    }
}
