package com.yummy.naraka.world.item.reinforcement;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public abstract class AttributeModifyingEffect implements ReinforcementEffect {
    protected final Attribute attribute;
    protected final Set<EquipmentSlot> slots;
    protected final EquipmentSlot.Type slotType;

    private static Set<EquipmentSlot> getSlots(EquipmentSlot.Type slotType) {
        Set<EquipmentSlot> slots = new HashSet<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == slotType)
                slots.add(slot);
        }

        return Set.copyOf(slots);
    }

    protected static String modifierId(EquipmentSlot slot, String name) {
        return "reinforcement_effect." + slot.name().toLowerCase() + "." + name;
    }

    public static AttributeModifyingEffect simple(Attribute attribute, EquipmentSlot.Type slotType) {
        return simple(attribute, slotType, reinforcement -> reinforcement, true);
    }

    public static AttributeModifyingEffect simple(Attribute attribute, EquipmentSlot.Type slotType, Function<Integer, Integer> modifyingValueByReinforcement, boolean showInTooltip) {
        final String modifierName = attribute.getDescriptionId();

        return new AttributeModifyingEffect(attribute, slotType) {
            @Override
            protected AttributeModifier createModifier(EquipmentSlot slot, int reinforcement) {
                return new AttributeModifier(
                        modifierId(slot, modifierName),
                        modifyingValueByReinforcement.apply(reinforcement),
                        AttributeModifier.Operation.ADDITION
                );
            }

            @Override
            public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
                return equipmentSlot.getType() == slotType;
            }

            @Override
            public boolean showInTooltip(int reinforcement) {
                return showInTooltip;
            }
        };
    }

    protected AttributeModifyingEffect(Attribute attribute, EquipmentSlot.Type slotType) {
        this.attribute = attribute;
        this.slots = getSlots(slotType);
        this.slotType = slotType;
    }

    @Override
    public Set<EquipmentSlot> getAvailableSlots() {
        return slots;
    }

    protected abstract AttributeModifier createModifier(EquipmentSlot slot, int reinforcement);

    @Override
    public void onReinforcementIncreased(ItemStack itemStack, int previousReinforcement, int currentReinforcement) {
        if (itemStack.getItem() instanceof Equipable equipable) {
            EquipmentSlot slot = equipable.getEquipmentSlot();
            itemStack.addAttributeModifier(attribute, createModifier(slot, currentReinforcement), slot);
        }
    }
}
