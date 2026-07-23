package com.yummy.naraka.world.item.tooltip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.event.ItemEvents;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Consumer;

public record DynamicItemLore(List<ConditionalComponents> conditional) implements ItemEvents.ItemTooltip {
    public static final Codec<DynamicItemLore> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ConditionalComponents.CODEC.listOf().optionalFieldOf("conditional", List.of()).forGetter(DynamicItemLore::conditional)
            ).apply(instance, DynamicItemLore::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DynamicItemLore> STREAM_CODEC = StreamCodec.composite(
            ConditionalComponents.STREAM_CODEC.apply(ByteBufCodecs.list()),
            DynamicItemLore::conditional,
            DynamicItemLore::new
    );

    public static final DynamicItemLore EMPTY = new DynamicItemLore(List.of());


    public static DynamicItemLore of(ConditionalComponents... conditionalComponents) {
        return new DynamicItemLore(List.of(conditionalComponents));
    }

    @Override
    public void addToTooltip(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, boolean shiftKeyPressed, Consumer<Component> builder) {
        for (ConditionalComponents components : conditional) {
            if (components.isAcceptable(item)) {
                components.addToTooltip(item, context, player, tooltipFlag, shiftKeyPressed, builder);
                return;
            }
        }
    }
}
