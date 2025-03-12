package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public abstract class AttributeModifyingEffect implements ReinforcementEffect {
    protected final Holder<Attribute> attribute;
    protected final Set<EquipmentSlot> slots;
    protected final EquipmentSlotGroup slotGroup;

    private static Set<EquipmentSlot> getSlots(EquipmentSlotGroup slotGroup) {
        Set<EquipmentSlot> slots = new HashSet<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slotGroup.test(slot))
                slots.add(slot);
        }

        return Set.copyOf(slots);
    }

    public static AttributeModifyingEffect simple(Holder<Attribute> attribute, EquipmentSlotGroup slotGroup) {
        return simple(attribute, slotGroup, reinforcement -> reinforcement, true);
    }

    public static AttributeModifyingEffect simple(Holder<Attribute> attribute, EquipmentSlotGroup slotGroup, Function<Integer, Integer> modifyingValueByReinforcement, boolean showInTooltip) {
        final String modifierName = attribute.unwrapKey()
                .map(ResourceKey::location)
                .map(ResourceLocation::getPath)
                .orElse("unidentified")
                .replaceAll(".*\\.", "");

        return new AttributeModifyingEffect(attribute, slotGroup) {
            @Override
            protected AttributeModifier createModifier(int reinforcement) {
                return new AttributeModifier(
                        modifierId(modifierName),
                        modifyingValueByReinforcement.apply(reinforcement),
                        AttributeModifier.Operation.ADD_VALUE
                );
            }

            @Override
            public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
                return slotGroup.test(equipmentSlot);
            }

            @Override
            public boolean showInTooltip(int reinforcement) {
                return showInTooltip;
            }
        };
    }

    protected AttributeModifyingEffect(Holder<Attribute> attribute, EquipmentSlotGroup slotGroup) {
        this.attribute = attribute;
        this.slots = getSlots(slotGroup);
        this.slotGroup = slotGroup;
    }

    @Override
    public Set<EquipmentSlot> getAvailableSlots() {
        return slots;
    }

    protected static ResourceLocation modifierId(String name) {
        return NarakaMod.location("reinforcement_effect", name);
    }

    protected abstract AttributeModifier createModifier(int reinforcement);

    @Override
    public void onReinforcementIncreased(ItemStack itemStack, int previousReinforcement, int currentReinforcement) {
        ItemAttributeModifiers modifiers = NarakaItemUtils.getAttributeModifiers(itemStack);
        itemStack.set(
                DataComponents.ATTRIBUTE_MODIFIERS,
                modifiers.withModifierAdded(
                        attribute,
                        createModifier(currentReinforcement),
                        slotGroup
                )
        );
    }
}
