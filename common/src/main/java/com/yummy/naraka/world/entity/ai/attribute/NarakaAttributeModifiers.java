package com.yummy.naraka.world.entity.ai.attribute;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * Mod {@linkplain AttributeModifier}s and helping methods<br>
 * To prevent overflow use {@link AttributeModifier.Operation#ADD_VALUE}
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
    public static final AttributeModifier STAGGERING_PREVENT_MOVING = preventMoving("weakness");

    public static final ResourceLocation REDUCE_MAX_HEALTH_ID = reduceMaxHealthId("locked_health");

    public static AttributeModifier reduceMaxHealth(ResourceLocation id, double value) {
        return new AttributeModifier(id, -value, AttributeModifier.Operation.ADD_VALUE);
    }

    public static ResourceLocation reduceMaxHealthId(String identifier) {
        return NarakaMod.location(identifier + ".reduce_max_health");
    }

    public static AttributeModifier preventMoving(String identifier) {
        return new AttributeModifier(preventMovingId(identifier), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public static ResourceLocation preventMovingId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_moving");
    }

    public static AttributeModifier preventJumping(String identifier) {
        return new AttributeModifier(preventJumpingId(identifier), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public static ResourceLocation preventJumpingId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_jumping");
    }

    public static AttributeModifier preventBlockAttack(String identifier) {
        return new AttributeModifier(preventBlockAttackId(identifier), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public static ResourceLocation preventBlockAttackId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_block_attack");
    }

    public static AttributeModifier preventEntityAttack(String identifier) {
        return new AttributeModifier(preventEntityAttackId(identifier), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public static ResourceLocation preventEntityInteractionId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_entity_interaction");
    }

    public static AttributeModifier preventEntityInteraction(String identifier) {
        return new AttributeModifier(preventEntityInteractionId(identifier), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public static ResourceLocation preventBlockInteractionId(String identifier) {
        return NarakaMod.location(identifier + ".prevent_block_interaction");
    }

    public static AttributeModifier preventBlockInteraction(String identifier) {
        return new AttributeModifier(preventBlockInteractionId(identifier), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
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
    public static void addAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance != null)
            instance.addOrUpdateTransientModifier(modifier);
    }

    public static void addPermanentModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance != null)
            instance.addOrReplacePermanentModifier(modifier);
    }

    /**
     * Remove {@linkplain AttributeModifier} of given entity
     *
     * @param livingEntity Entity to remove modifier
     * @param attribute    Attribute to remove modifier
     * @param modifier     Attribute modifier
     */
    public static void removeAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        removeAttributeModifier(livingEntity, attribute, modifier.id());
    }

    /**
     * Remove {@linkplain AttributeModifier} of given entity
     *
     * @param livingEntity Entity to remove modifier
     * @param attribute    Attribute to remove modifier
     * @param modifierId   ID of attribute modifier
     */
    public static void removeAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, ResourceLocation modifierId) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return;
        instance.removeModifier(modifierId);
    }

    public static boolean hasAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, AttributeModifier modifier) {
        return hasAttributeModifier(livingEntity, attribute, modifier.id());
    }

    public static boolean hasAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, ResourceLocation modifierId) {
        AttributeInstance instance = livingEntity.getAttribute(attribute);
        if (instance == null)
            return false;
        return instance.hasModifier(modifierId);
    }
}
