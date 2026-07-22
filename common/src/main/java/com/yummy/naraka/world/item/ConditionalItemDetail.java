package com.yummy.naraka.world.item;

import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;
import java.util.function.Function;

public record ConditionalItemDetail(
        Function<ItemStack, ItemEvents.ItemTooltip> selector) implements ItemEvents.ItemTooltip {
    public static ConditionalItemDetail hasNbt(String key, ItemEvents.ItemTooltip has, ItemEvents.ItemTooltip fallback) {
        return new ConditionalItemDetail(itemStack -> {
            if (NarakaItemUtils.hasNbtData(itemStack, key))
                return has;
            return fallback;
        });
    }

    @Override
    public void addToTooltip(ItemStack itemStack, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        selector.apply(itemStack).addToTooltip(itemStack, player, tooltipFlag, builder);
    }
}
