package com.yummy.naraka.world.entity.ai.attribute;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * Mod {@linkplain AttributeModifier}s and helping methods<br>
 * To prevent overflow use {@link AttributeModifier.Operation#ADDITION}
 *
 * @author lalaalal
 */
public class NarakaAttributeModifiers {
    public static final AttributeModifier STUN_PREVENT_MOVING = preventMoving("stun");
    public static final AttributeModifier STUN_PREVENT_JUMPING = preventJumping("stun");
    public static final AttributeModifier STUN_PREVENT_BLOCK_ATTACK = preventBlockAttack("stun");
    public static final AttributeModifier STUN_PREVENT_ENTITY_ATTACK = preventEntityAttack("stun");
    public static final AttributeModifier STUN_PREVENT_BLOCK_INTERACTION = preventBlockInteraction("stun");
    public static final AttributeModifier STUN_PREVENT_ENTITY_INTERACTION = preventEntityInteraction("stun");

    public static final AttributeModifier HIBERNATE_PREVENT_MOVING = preventMoving("hibernate");
    public static final AttributeModifier ANIMATION_PREVENT_MOVING = preventMoving("animation");

    public static final ResourceLocation REDUCE_MAX_HEALTH_ID = reduceMaxHealthId("locked_health");

    public static final AttributeModifier FINAL_HEROBRINE_ARMOR_TOUGHNESS = new AttributeModifier(
            "final_herobrine.armor_toughness", 16, AttributeModifier.Operation.ADDITION
    );

    public static AttributeModifier finalHerobrineArmor(int armor) {
        return new AttributeModifier(
                "final_herobrine.armor", armor, AttributeModifier.Operation.ADDITION
        );
    }

    public static AttributeModifier reduceMaxHealth(ResourceLocation id, double value) {
        return new AttributeModifier(id.getPath(), -value, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static ResourceLocation reduceMaxHealthId(String identifier) {
        return NarakaMod.location(identifier + ".reduce_max_health");
    }

    public static AttributeModifier preventMoving(String identifier) {
        return new AttributeModifier(preventMovingId(identifier).getPath(), -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static ResourceLocation preventMovingId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_moving");
    }

    public static AttributeModifier preventJumping(String identifier) {
        return new AttributeModifier(preventJumpingId(identifier).getPath(), -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static ResourceLocation preventJumpingId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_jumping");
    }

    public static AttributeModifier preventBlockAttack(String identifier) {
        return new AttributeModifier(preventBlockAttackId(identifier).getPath(), -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static ResourceLocation preventBlockAttackId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_block_attack");
    }

    public static AttributeModifier preventEntityAttack(String identifier) {
        return new AttributeModifier(preventEntityAttackId(identifier).getPath(), -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static ResourceLocation preventEntityInteractionId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_entity_interaction");
    }

    public static AttributeModifier preventEntityInteraction(String identifier) {
        return new AttributeModifier(preventEntityInteractionId(identifier).getPath(), -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static ResourceLocation preventBlockInteractionId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_block_interaction");
    }

    public static AttributeModifier preventBlockInteraction(String identifier) {
        return new AttributeModifier(preventBlockInteractionId(identifier).getPath(), -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static ResourceLocation preventEntityAttackId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_entity_attack");
    }

    /**
     * Add {@linkplain AttributeModifier} to given entity
     *
     * @param livingEntity Entity to add {@linkplain AttributeModifier}
     * @param attribute    Attribute to add modifier
     * @param modifier     Attribute modifier
     */
    public static void addAttributeModifier(LivingEntity livingEntity, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance != null)
            instance.addTransientModifier(modifier);
    }

    public static void addPermanentModifier(LivingEntity livingEntity, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance != null)
            instance.addTransientModifier(modifier);
    }

    /**
     * Remove {@linkplain AttributeModifier} of given entity
     *
     * @param livingEntity Entity to remove modifier
     * @param attribute    Attribute to remove modifier
     * @param modifier     Attribute modifier
     */
    public static void removeAttributeModifier(LivingEntity livingEntity, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return;
        instance.removeModifier(modifier);
    }

    public static boolean hasAttributeModifier(LivingEntity livingEntity, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return false;
        return instance.hasModifier(modifier);
    }
}
