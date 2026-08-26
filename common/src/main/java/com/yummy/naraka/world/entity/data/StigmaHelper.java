package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.util.NarakaEntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class StigmaHelper {
    public static Stigma get(LivingEntity livingEntity) {
        return EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.STIGMA.get());
    }

    private static void set(LivingEntity livingEntity, Stigma stigma) {
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.STIGMA.get(), stigma);
    }

    public static boolean hasStigma(LivingEntity livingEntity) {
        return get(livingEntity).value() > 0;
    }

    public static void increaseStigma(ServerLevel level, LivingEntity target, LivingEntity cause) {
        if (target.getType().is(NarakaEntityTypeTags.STIGMA_IMMUNE) || !NarakaConfig.COMMON.enableStigma.getValue()
                || !NarakaEntityUtils.isDamageable(target))
            return;
        Stigma stigma = get(target);
        Stigma increased = stigma.increase(level, target, cause);
        set(target, increased);
    }

    public static void decreaseStigma(LivingEntity livingEntity) {
        Stigma stigma = get(livingEntity);
        Stigma decreased = stigma.decrease();

        set(livingEntity, decreased);
    }

    public static void removeStigma(LivingEntity livingEntity) {
        EntityDataHelper.removeEntityData(livingEntity, NarakaEntityDataTypes.STIGMA.get());
    }

    public static void tick(ServerLevel level, LivingEntity livingEntity, Stigma stigma) {
        if (stigma.value() > 0) {
            if (consumeStigmaAfter(level, livingEntity))
                removeStigma(livingEntity);
        }
    }

    /**
     * Consumes the stigma when the given tickAfter has elapsed from {@linkplain Stigma#lastMarkedTime()}
     *
     * @param livingEntity Stigmatized entity
     * @return True if stigma should be removed
     * @see Stigma#consume(ServerLevel, LivingEntity, LivingEntity)
     */
    public static boolean consumeStigmaAfter(ServerLevel level, LivingEntity livingEntity) {
        int stigmaConsumeTick = NarakaConfig.COMMON.stigmaConsumeTick.getValue();
        Stigma stigma = get(livingEntity);
        Optional<LivingEntity> cause = stigma.getCause(level);
        if (cause.isEmpty())
            return true;

        long currentGameTime = livingEntity.level().getGameTime();
        if (stigma.value() > 0 && currentGameTime > stigma.lastMarkedTime() + stigmaConsumeTick) {
            Stigma consumed = stigma.consume(level, livingEntity, cause.get());
            set(livingEntity, consumed);
            return true;
        }
        return false;
    }

    public static void initialize() {

    }
}
