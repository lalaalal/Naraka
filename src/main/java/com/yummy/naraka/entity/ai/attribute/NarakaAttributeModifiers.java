package com.yummy.naraka.entity.ai.attribute;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

/**
 * Mod {@linkplain AttributeModifier}s and helping methods
 *
 * @author lalaalal
 */
public class NarakaAttributeModifiers {
    public static final AttributeModifier BLOCK_MOVING = new AttributeModifier(UUID.fromString("e142c26c-6db7-45de-b651-f555a4122611"), "Movement Speed", -0.15f * 256, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier BLOCK_JUMPING = new AttributeModifier(UUID.fromString("a287040c-731e-4e22-ac5c-6b8a76b0ca53"), "Jump Strength", -256f, AttributeModifier.Operation.ADD_VALUE);

    public static AttributeModifier maxHealthModifier(float amount, AttributeModifier.Operation operation) {
        return new AttributeModifier(UUID.randomUUID(), "max Health", amount, operation);
    }

    /**
     * Add {@linkplain AttributeModifier} to given entity
     *
     * @param livingEntity Entity to add {@linkplain AttributeModifier}
     * @param attribute Attribute to add modifier
     * @param modifier Attribute modifier
     *
     * @see net.minecraft.world.entity.ai.attributes.Attributes
     */
    public static void addAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return;
        instance.addOrUpdateTransientModifier(modifier);
    }

    /**
     * Remove {@linkplain AttributeModifier} of given entity
     *
     * @param livingEntity Entity to remove modifier
     * @param attribute Attribute to remove modifier
     * @param modifier Attribute modifier
     *
     * @see net.minecraft.world.entity.ai.attributes.Attributes
     */
    public static void removeAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return;
        instance.removeModifier(modifier);
    }
}
