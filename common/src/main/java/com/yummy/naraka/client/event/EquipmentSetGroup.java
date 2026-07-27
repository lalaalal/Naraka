package com.yummy.naraka.client.event;

import com.mojang.serialization.Codec;
import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import com.yummy.naraka.world.item.equipmentset.EquipmentSet;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record EquipmentSetGroup(List<EquipmentSet> equipmentSets) implements ItemEvents.ItemTooltip {
    public static final Codec<EquipmentSetGroup> CODEC = EquipmentSet.CODEC.listOf()
            .xmap(EquipmentSetGroup::new, EquipmentSetGroup::equipmentSets);
    public static final StreamCodec<RegistryFriendlyByteBuf, EquipmentSetGroup> STREAM_CODEC = StreamCodec.composite(
            EquipmentSet.STREAM_CODEC.apply(ByteBufCodecs.list()),
            EquipmentSetGroup::equipmentSets,
            EquipmentSetGroup::new
    );

    public static final EquipmentSetGroup EMPTY = new EquipmentSetGroup(List.of());

    public static EquipmentSetGroup of(EquipmentSet... equipmentSets) {
        return new EquipmentSetGroup(List.of(equipmentSets));
    }

    public boolean isEmpty() {
        return equipmentSets.isEmpty();
    }

    public void update(LivingEntity livingEntity) {
        if (isEmpty())
            return;

        List<EquipmentSet> activeEquipmentSets = new ArrayList<>(
                EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.ACTIVE_EQUIPMENT_SET_GROUP.get())
                        .equipmentSets()
        );
        for (EquipmentSet equipmentSet : equipmentSets)
            updateSingle(activeEquipmentSets, equipmentSet, livingEntity);
        EquipmentSetGroup newGroup = new EquipmentSetGroup(activeEquipmentSets);
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.ACTIVE_EQUIPMENT_SET_GROUP.get(), newGroup);
    }

    private void updateSingle(List<EquipmentSet> activeEquipmentSets, EquipmentSet equipmentSet, LivingEntity livingEntity) {
        if (equipmentSet.updateEffect(livingEntity)) {
            if (!activeEquipmentSets.contains(equipmentSet)) {
                activeEquipmentSets.add(equipmentSet);
            }
        } else {
            activeEquipmentSets.remove(equipmentSet);
        }
    }

    public boolean contains(Identifier id) {
        return equipmentSets.stream().anyMatch(equipmentSet -> equipmentSet.getId().equals(id));
    }

    @Override
    public void addToTooltip(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, boolean shiftKeyPressed, Consumer<Component> builder) {
        for (EquipmentSet equipmentSet : equipmentSets)
            equipmentSet.addToTooltip(item, context, player, tooltipFlag, shiftKeyPressed, builder);
    }
}
