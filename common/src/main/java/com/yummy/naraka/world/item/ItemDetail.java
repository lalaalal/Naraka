package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.yummy.naraka.event.ItemEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Consumer;

public record ItemDetail(List<Component> lines) implements ItemEvents.ItemTooltip {
    public static final ItemDetail EMPTY = new ItemDetail(List.of());

    public static final Codec<ItemDetail> CODEC = ExtraCodecs.FLAT_COMPONENT.listOf()
            .xmap(ItemDetail::new, ItemDetail::lines);

    @Override
    public void addToTooltip(ItemStack itemStack, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        for (Component line : lines)
            builder.accept(line);
    }
}
