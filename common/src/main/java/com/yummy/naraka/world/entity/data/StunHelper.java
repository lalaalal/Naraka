package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.tags.NarakaEntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public final class StunHelper {
    public static void holdEntity(LivingEntity livingEntity) {
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(true);
    }

    public static void releaseEntity(LivingEntity livingEntity) {
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(false);
    }

    public static boolean isStun(LivingEntity livingEntity) {
        return EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.STUN_TICK.getConcreteValue()) > 0;
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

        int previousStunTick = EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.STUN_TICK.getConcreteValue());
        if (previousStunTick > duration || (previousStunTick > 0 && !update))
            return;

        holdEntity(livingEntity);
        livingEntity.stopUsingItem();
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STUN_TICK.getConcreteValue(), duration);
    }
}
