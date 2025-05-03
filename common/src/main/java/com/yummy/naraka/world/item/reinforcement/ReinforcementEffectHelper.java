package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.world.entity.LivingEntity;

public class ReinforcementEffectHelper {
    public static float increaseSpeedInLiquid(LivingEntity entity, float scale) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(entity)) {
            float speedModifier = NarakaConfig.COMMON.fasterLiquidSwimmingSpeed.getValue();
            if (entity.isInLava() && entity.isSwimming())
                return scale * speedModifier * 3;
            return scale * speedModifier;
        }
        return scale;
    }

    public static boolean considerLiquidAsWater(LivingEntity entity, boolean original) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(entity))
            return entity.isInLiquid();
        return original;
    }

    public static boolean ignoreEyeInWaterWithEfficientMiningInWater(LivingEntity entity, boolean original) {
        if (NarakaItemUtils.canApplyEfficientMiningInWater(entity))
            return false;
        return original;
    }

    public static boolean considerOnGroundWithEfficientMimingInAir(LivingEntity entity, boolean original) {
        if (NarakaItemUtils.canApplyEfficientMiningInAir(entity))
            return true;
        return original;
    }
}
