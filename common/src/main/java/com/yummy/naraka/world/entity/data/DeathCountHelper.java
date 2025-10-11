package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.DeathCountingEntity;
import net.minecraft.sounds.SoundEvents;
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
        if (!deathCountingEntity.living().level().isClientSide() && !DEATH_COUNTING_ENTITIES.contains(deathCountingEntity))
            DEATH_COUNTING_ENTITIES.add(deathCountingEntity);
    }

    public static void detachCountingEntity(DeathCountingEntity deathCountingEntity) {
        if (DEATH_COUNTING_ENTITIES.contains(deathCountingEntity)) {
            DEATH_COUNTING_ENTITIES.remove(deathCountingEntity);
            deathCountingEntity.releaseAllCounting();
        }
    }

    public static int get(LivingEntity livingEntity) {
        return EntityDataHelper.getEntityData(livingEntity, NarakaEntityDataTypes.DEATH_COUNT.get());
    }

    public static boolean isDeathCounted(LivingEntity livingEntity) {
        return get(livingEntity) > NOT_APPLIED;
    }

    public static void applyDeathCount(LivingEntity livingEntity) {
        int deathCount = get(livingEntity);
        if (deathCount == NOT_APPLIED && isDeathCountable(livingEntity))
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.DEATH_COUNT.get(), MAX_DEATH_COUNT);
    }

    public static void removeDeathCount(LivingEntity livingEntity) {
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.DEATH_COUNT.get(), NOT_APPLIED);
    }

    /**
     * Use death count
     *
     * @param livingEntity Entity using death count
     * @return True if entity should be alive
     */
    public static boolean useDeathCount(LivingEntity livingEntity) {
        int deathCount = get(livingEntity);
        if (deathCount > 1) {
            EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.DEATH_COUNT.get(), deathCount - 1);
            livingEntity.setHealth(livingEntity.getMaxHealth());
            livingEntity.level().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.TOTEM_USE, livingEntity.getSoundSource(), 1.0F, 1.0F);
        } else {
            for (DeathCountingEntity deathCountingEntity : DEATH_COUNTING_ENTITIES)
                deathCountingEntity.forget(livingEntity);
        }
        if (deathCount > 0) {
            for (DeathCountingEntity deathCountingEntity : DEATH_COUNTING_ENTITIES) {
                if (deathCountingEntity.isCounting(livingEntity))
                    deathCountingEntity.onEntityUseDeathCount(livingEntity);
            }
        }
        return deathCount > 1;
    }
}
