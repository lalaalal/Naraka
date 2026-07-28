package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record EquipmentSetGroup(List<EquipmentSet> equipmentSets) implements ItemEvents.ItemTooltip {
    public static final Codec<EquipmentSetGroup> CODEC = EquipmentSet.CODEC.listOf()
            .xmap(EquipmentSetGroup::new, EquipmentSetGroup::equipmentSets);

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
                EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.ACTIVE_EQUIPMENT_SET_GROUP.getConcreteValue())
                        .equipmentSets()
        );
        for (EquipmentSet equipmentSet : equipmentSets)
            updateSingle(activeEquipmentSets, equipmentSet, livingEntity);
        EquipmentSetGroup newGroup = new EquipmentSetGroup(activeEquipmentSets);
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.ACTIVE_EQUIPMENT_SET_GROUP.getConcreteValue(), newGroup);
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

    public boolean contains(ResourceLocation id) {
        return equipmentSets.stream().anyMatch(equipmentSet -> equipmentSet.getId().equals(id));
    }

    @Override
    public void addToTooltip(ItemStack item, Player player, TooltipFlag tooltipFlag, boolean shiftKeyPressed, Consumer<Component> builder) {
        for (EquipmentSet equipmentSet : equipmentSets)
            equipmentSet.addToTooltip(item, player, tooltipFlag, shiftKeyPressed, builder);
    }
}
