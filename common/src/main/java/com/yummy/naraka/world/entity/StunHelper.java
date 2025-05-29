package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.TickSchedule;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;

public final class StunHelper {
    private static final Map<LivingEntity, TickSchedule> STUN_RELEASE_SCHEDULES = new HashMap<>();

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
        STUN_RELEASE_SCHEDULES.remove(livingEntity);
    }

    private static TickSchedule createReleaseSchedule(LivingEntity livingEntity, int tickAfter) {
        long gameTime = livingEntity.level().getGameTime();
        return TickSchedule.executeAfter(gameTime, tickAfter, () -> releaseEntity(livingEntity));
    }

    public static boolean isStun(LivingEntity livingEntity) {
        return NarakaAttributeModifiers.hasAttributeModifier(livingEntity, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.STUN_PREVENT_MOVING);
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
        holdEntity(livingEntity);
        livingEntity.stopUsingItem();
        long gameTime = livingEntity.level().getGameTime();
        TickSchedule schedule = STUN_RELEASE_SCHEDULES.computeIfAbsent(livingEntity, key -> createReleaseSchedule(key, duration));
        if (schedule.isExpired(gameTime)) {
            schedule = createReleaseSchedule(livingEntity, duration);
        } else if (update && schedule.getTimeToRun() < gameTime + duration) {
            schedule.update(gameTime + duration);
        }
        STUN_RELEASE_SCHEDULES.put(livingEntity, schedule);
    }
}
