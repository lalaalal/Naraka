package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class DamageIncrease implements ReinforcementEffect {
    private static AttributeModifier createModifier(int reinforcement) {
        return new AttributeModifier(
                NarakaMod.location("reinforcement_effect", "damage_increment"),
                reinforcement,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Override
    public boolean canApply(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack, int reinforcement) {
        return equipmentSlot == EquipmentSlot.MAINHAND;
    }

    @Override
    public void onReinforcementIncreased(ItemStack itemStack, int previousReinforcement, int currentReinforcement) {
        ItemAttributeModifiers modifiers = NarakaItemUtils.getAttributeModifiers(itemStack);
        itemStack.set(
                DataComponents.ATTRIBUTE_MODIFIERS,
                modifiers.withModifierAdded(
                        Attributes.ATTACK_DAMAGE,
                        createModifier(currentReinforcement),
                        EquipmentSlotGroup.MAINHAND
                )
        );
    }
}
