package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.DeathCountingEntity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class DeathCountHelper {
    public static final int MAX_DEATH_COUNT = 5;
    public static final int NOT_APPLIED = -1;

    private static final List<DeathCountingEntity> DEATH_COUNTING_ENTITIES = new ArrayList<>();

    private static boolean isDeathCountable(LivingEntity livingEntity) {
        return livingEntity.getType().is(NarakaEntityTypeTags.DEATH_COUNTABLE)
                && !livingEntity.getType().is(NarakaEntityTypeTags.DEATH_COUNTING);
    }

    public static void attachCountingEntity(DeathCountingEntity deathCountingEntity) {
        if (!deathCountingEntity.living().level().isClientSide && !DEATH_COUNTING_ENTITIES.contains(deathCountingEntity))
            DEATH_COUNTING_ENTITIES.add(deathCountingEntity);
    }

    public static void detachCountingEntity(DeathCountingEntity deathCountingEntity) {
        if (DEATH_COUNTING_ENTITIES.contains(deathCountingEntity)) {
            DEATH_COUNTING_ENTITIES.remove(deathCountingEntity);
            deathCountingEntity.releaseAllCounting();
        }
    }

    public static int get(LivingEntity livingEntity) {
        return EntityDataHelper.getEntityData(livingEntity, EntityDataTypes.DEATH_COUNT);
    }

    public static boolean isDeathCounted(LivingEntity livingEntity) {
        return get(livingEntity) > NOT_APPLIED;
    }

    public static void applyDeathCount(LivingEntity livingEntity) {
        int deathCount = get(livingEntity);
        if (deathCount == NOT_APPLIED && isDeathCountable(livingEntity))
            EntityDataHelper.setEntityData(livingEntity, EntityDataTypes.DEATH_COUNT, MAX_DEATH_COUNT);
    }

    public static void removeDeathCount(LivingEntity livingEntity) {
        EntityDataHelper.setEntityData(livingEntity, EntityDataTypes.DEATH_COUNT, NOT_APPLIED);
    }

    public static boolean useDeathCount(LivingEntity livingEntity) {
        int deathCount = get(livingEntity);
        if (deathCount > 1) {
            EntityDataHelper.setEntityData(livingEntity, EntityDataTypes.DEATH_COUNT, deathCount - 1);
            livingEntity.setHealth(livingEntity.getMaxHealth());
        } else {
            for (DeathCountingEntity deathCountingEntity : DEATH_COUNTING_ENTITIES)
                deathCountingEntity.forget(livingEntity);
        }
        return deathCount > 1;
    }
}
