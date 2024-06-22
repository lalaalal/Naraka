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
    public static final AttributeModifier BLOCK_MOVING = new AttributeModifier(NarakaMod.location("movement_speed"), -0.15f * 256, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier BLOCK_JUMPING = new AttributeModifier(NarakaMod.location("jump_strength"), -256f, AttributeModifier.Operation.ADD_VALUE);

    public static AttributeModifier impaling(int level) {
        return new AttributeModifier(NarakaMod.location("impaling_damage"), level, AttributeModifier.Operation.ADD_VALUE);
    }

    /**
     * Add {@linkplain AttributeModifier} to given entity
     *
     * @param livingEntity Entity to add {@linkplain AttributeModifier}
     * @param attribute    Attribute to add modifier
     * @param modifier     Attribute modifier
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
     * @param attribute    Attribute to remove modifier
     * @param modifier     Attribute modifier
     * @see net.minecraft.world.entity.ai.attributes.Attributes
     */
    public static void removeAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return;
        instance.removeModifier(modifier);
    }
}
