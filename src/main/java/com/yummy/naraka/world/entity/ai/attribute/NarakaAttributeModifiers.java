package com.yummy.naraka.world.entity.ai.attribute;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.TickSchedule;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Mod {@linkplain AttributeModifier}s and helping methods
 *
 * @author lalaalal
 */
public class NarakaAttributeModifiers {
    private static final Map<LivingEntity, Long> STUN_RELEASE_TIME = new HashMap<>();

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


    public static void holdEntity(LivingEntity livingEntity) {
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.MOVEMENT_SPEED,
                NarakaAttributeModifiers.PREVENT_MOVING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.JUMP_STRENGTH,
                NarakaAttributeModifiers.PREVENT_JUMPING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.ATTACK_DAMAGE,
                NarakaAttributeModifiers.PREVENT_ENTITY_ATTACKING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.BLOCK_BREAK_SPEED,
                NarakaAttributeModifiers.PREVENT_BLOCK_ATTACKING
        );
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(true);
    }

    public static void releaseEntity(LivingEntity livingEntity) {
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.MOVEMENT_SPEED,
                NarakaAttributeModifiers.PREVENT_MOVING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.JUMP_STRENGTH,
                NarakaAttributeModifiers.PREVENT_JUMPING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.ATTACK_DAMAGE,
                NarakaAttributeModifiers.PREVENT_ENTITY_ATTACKING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.BLOCK_BREAK_SPEED,
                NarakaAttributeModifiers.PREVENT_BLOCK_ATTACKING
        );
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(false);
    }

    public static void stunEntity(LivingEntity livingEntity, int duration) {
        holdEntity(livingEntity);
        STUN_RELEASE_TIME.put(livingEntity, livingEntity.level().getGameTime());
        TickSchedule.executeOn(
                gameTime -> STUN_RELEASE_TIME.getOrDefault(livingEntity, gameTime) + duration <= gameTime,
                () -> releaseEntity(livingEntity)
        );
    }
}
