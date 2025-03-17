package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LockedHealthHelper {
    /**
     * Reduce max health
     *
     * @param target Target entity to reduce max health
     * @param amount Amount of max health to be reduced
     */
    public static void lock(LivingEntity target, double amount) {
        if (NarakaEntityUtils.isDamageable(target)) {
            AttributeModifier maxHealthModifier = NarakaAttributeModifiers.reduceMaxHealth(amount);
            NarakaAttributeModifiers.addPermanentModifier(target, Attributes.MAX_HEALTH, maxHealthModifier);
            EntityDataHelper.setEntityData(target, NarakaEntityDataTypes.LOCKED_HEALTH.get(), amount);
        }
    }

    /**
     * Restore original max health
     *
     * @param target Target entity to restore max health
     */
    public static void release(LivingEntity target) {
        NarakaAttributeModifiers.removeAttributeModifier(target, Attributes.MAX_HEALTH, NarakaAttributeModifiers.REDUCE_MAX_HEALTH_ID);
        EntityDataHelper.setEntityData(target, NarakaEntityDataTypes.LOCKED_HEALTH.get(), 0.0);
    }
}
