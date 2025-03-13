package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LockedHealthHelper {
    public static void lock(LivingEntity target, double amount) {
        AttributeModifier maxHealthModifier = NarakaAttributeModifiers.reduceMaxHealth(amount);
        NarakaAttributeModifiers.addPermanentModifier(target, Attributes.MAX_HEALTH, maxHealthModifier);
        EntityDataHelper.setEntityData(target, NarakaEntityDataTypes.LOCKED_HEALTH.get(), amount);
    }

    public static void release(LivingEntity target) {
        NarakaAttributeModifiers.removeAttributeModifier(target, Attributes.MAX_HEALTH, NarakaAttributeModifiers.REDUCE_MAX_HEALTH_ID);
        EntityDataHelper.setEntityData(target, NarakaEntityDataTypes.LOCKED_HEALTH.get(), 0.0);
    }
}
