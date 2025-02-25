package com.yummy.naraka.world.entity.ai.attribute;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * Mod {@linkplain AttributeModifier}s and helping methods
 *
 * @author lalaalal
 */
public class NarakaAttributeModifiers {
    public static final AttributeModifier PREVENT_MOVING = new AttributeModifier(NarakaMod.location("prevent_moving"), -0.15f * 256, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier PREVENT_JUMPING = new AttributeModifier(NarakaMod.location("prevent_jumping"), -256f, AttributeModifier.Operation.ADD_VALUE);
    public static final AttributeModifier PREVENT_BLOCK_ATTACKING = new AttributeModifier(NarakaMod.location("prevent_attack_block"), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier PREVENT_ENTITY_ATTACKING = new AttributeModifier(NarakaMod.location("prevent_attack_entity"), -4.0 * 256, AttributeModifier.Operation.ADD_VALUE);

    /**
     * Add {@linkplain AttributeModifier} to given entity
     *
     * @param livingEntity Entity to add {@linkplain AttributeModifier}
     * @param attribute    Attribute to add modifier
     * @param modifier     Attribute modifier
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
     * @param attribute    Attribute to remove modifier
     * @param modifier     Attribute modifier
     */
    public static void removeAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return;
        instance.removeModifier(modifier);
    }

    public static boolean hasAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return false;
        return instance.hasModifier(modifier.id());
    }
}
