package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public abstract class AttributeModifyingEffect implements ReinforcementEffect {
    protected final Holder<Attribute> attribute;

    protected AttributeModifyingEffect(Holder<Attribute> attribute) {
        this.attribute = attribute;
    }

    protected static ResourceLocation modifierId(String name) {
        return NarakaMod.location("reinforcement_effect", name);
    }

    protected abstract AttributeModifier createModifier(int reinforcement);

    protected abstract EquipmentSlotGroup getTargetSlot(ItemStack itemStack);

    @Override
    public void onReinforcementIncreased(ItemStack itemStack, int previousReinforcement, int currentReinforcement) {
        ItemAttributeModifiers modifiers = NarakaItemUtils.getAttributeModifiers(itemStack);
        itemStack.set(
                DataComponents.ATTRIBUTE_MODIFIERS,
                modifiers.withModifierAdded(
                        attribute,
                        createModifier(currentReinforcement),
                        getTargetSlot(itemStack)
                )
        );
    }
}
