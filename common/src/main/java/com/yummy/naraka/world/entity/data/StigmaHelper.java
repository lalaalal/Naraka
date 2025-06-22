package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StigmaHelper {
    public static Stigma get(LivingEntity livingEntity) {
        return EntityDataHelper.getEntityData(livingEntity, NarakaEntityDataTypes.STIGMA.get());
    }

    private static void set(LivingEntity livingEntity, Stigma stigma) {
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STIGMA.get(), stigma);
    }

    public static void increaseStigma(ServerLevel level, LivingEntity target, Entity cause, boolean recordTime) {
        if (target.getType().is(NarakaEntityTypeTags.HEROBRINE) || NarakaConfig.COMMON.disableStigma.getValue())
            return;
        Stigma stigma = get(target);
        Stigma increased = stigma.increase(level, target, cause, recordTime);
        set(target, increased);
    }

    public static void increaseStigma(ServerLevel level, LivingEntity target, Entity cause) {
        increaseStigma(level, target, cause, false);
    }

    public static void decreaseStigma(LivingEntity livingEntity) {
        long currentGameTime = livingEntity.level().getGameTime();
        Stigma stigma = get(livingEntity);
        Stigma decreased = stigma.decrease(currentGameTime);

        set(livingEntity, decreased);
    }

    public static void removeStigma(LivingEntity livingEntity) {
        set(livingEntity, Stigma.ZERO);
    }

    /**
     * Consumes the stigma when the given tickAfter has elapsed from {@linkplain Stigma#lastMarkedTime()}
     *
     * @param livingEntity Stigmatized entity
     * @param cause        Entity collects stigma
     * @param tickAfter    Minimum length required to collect stigma
     * @return True if succeed
     * @see Stigma#consume(ServerLevel, LivingEntity, Entity)
     */
    public static boolean collectStigmaAfter(ServerLevel level, LivingEntity livingEntity, Entity cause, int tickAfter) {
        Stigma stigma = get(livingEntity);
        long currentGameTime = livingEntity.level().getGameTime();
        if (stigma.value() > 0 && stigma.lastMarkedTime() != 0 && currentGameTime > stigma.lastMarkedTime() + tickAfter) {
            Stigma consumed = stigma.consume(level, livingEntity, cause);
            set(livingEntity, consumed);
            return true;
        }
        return false;
    }

    public static void initialize() {

    }
}
