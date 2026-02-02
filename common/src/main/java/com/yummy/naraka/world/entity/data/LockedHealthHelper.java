package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.util.NarakaEntityUtils;
import net.minecraft.world.entity.LivingEntity;

public class LockedHealthHelper {
    public static double get(LivingEntity livingEntity) {
        return EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.LOCKED_HEALTH.get());
    }

    /**
     * Reduce max health
     *
     * @param target Target entity to reduce max health
     * @param amount The amount of max health to be reduced
     */
    public static void lock(LivingEntity target, double amount) {
        if (NarakaEntityUtils.isDamageable(target)) {
            double lockedHealth = get(target);
            EntityDataHelper.setEntityData(target, NarakaEntityDataTypes.LOCKED_HEALTH.get(), lockedHealth + amount);
            target.setHealth((float) Math.min(target.getHealth(), target.getMaxHealth() - (lockedHealth + amount)));
        }
    }

    /**
     * Restore original max health
     *
     * @param target Target entity to restore max health
     */
    public static void release(LivingEntity target) {
        EntityDataHelper.setEntityData(target, NarakaEntityDataTypes.LOCKED_HEALTH.get(), 0.0);
    }
}
