package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class StunHelper {
    public static void holdEntity(LivingEntity livingEntity) {
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.MOVEMENT_SPEED,
                NarakaAttributeModifiers.STUN_PREVENT_MOVING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.JUMP_STRENGTH,
                NarakaAttributeModifiers.STUN_PREVENT_JUMPING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.ATTACK_DAMAGE,
                NarakaAttributeModifiers.STUN_PREVENT_ENTITY_ATTACK
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.BLOCK_BREAK_SPEED,
                NarakaAttributeModifiers.STUN_PREVENT_BLOCK_ATTACK
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.ENTITY_INTERACTION_RANGE,
                NarakaAttributeModifiers.STUN_PREVENT_ENTITY_INTERACTION
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.BLOCK_INTERACTION_RANGE,
                NarakaAttributeModifiers.STUN_PREVENT_BLOCK_INTERACTION
        );
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(true);
    }

    public static void releaseEntity(LivingEntity livingEntity) {
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.MOVEMENT_SPEED,
                NarakaAttributeModifiers.STUN_PREVENT_MOVING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.JUMP_STRENGTH,
                NarakaAttributeModifiers.STUN_PREVENT_JUMPING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.ATTACK_DAMAGE,
                NarakaAttributeModifiers.STUN_PREVENT_ENTITY_ATTACK
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.BLOCK_BREAK_SPEED,
                NarakaAttributeModifiers.STUN_PREVENT_BLOCK_ATTACK
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.ENTITY_INTERACTION_RANGE,
                NarakaAttributeModifiers.STUN_PREVENT_ENTITY_INTERACTION
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.BLOCK_INTERACTION_RANGE,
                NarakaAttributeModifiers.STUN_PREVENT_BLOCK_INTERACTION
        );
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(false);
    }

    public static boolean isStun(LivingEntity livingEntity) {
        return EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.STUN_TICK.get()) > 0;
    }

    /**
     * Block entity moving, jumping, using item, attacking for duration
     *
     * @param livingEntity Target entity to stun
     * @param duration     Stun duration
     */
    public static void stunEntity(LivingEntity livingEntity, int duration) {
        stunEntity(livingEntity, duration, false);
    }

    public static void stunEntity(LivingEntity livingEntity, int duration, boolean update) {
        if (livingEntity.getType().is(NarakaEntityTypeTags.STUN_IMMUNE) || duration == 0)
            return;

        int previousStunTick = EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.STUN_TICK.get());
        if (previousStunTick > duration || (previousStunTick > 0 && !update))
            return;

        holdEntity(livingEntity);
        livingEntity.stopUsingItem();
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STUN_TICK.get(), duration);
    }
}
