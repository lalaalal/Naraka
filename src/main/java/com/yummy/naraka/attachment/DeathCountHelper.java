package com.yummy.naraka.attachment;

import com.yummy.naraka.damagesource.NarakaDamageSources;
import com.yummy.naraka.event.NarakaGameEventBus;
import com.yummy.naraka.networking.payload.IntAttachmentSyncHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Help control death count
 *
 * @see NarakaAttachments#DEATH_COUNT
 *
 * @author lalaalal
 */
public class DeathCountHelper {
    public static final int DEFAULT_DEATH_COUNT = 5;

    public static int getDeathCount(LivingEntity livingEntity) {
        return livingEntity.getData(NarakaAttachments.DEATH_COUNT);
    }

    /**
     * Restore death count to {@linkplain DeathCountHelper#DEFAULT_DEATH_COUNT}
     *
     * @param livingEntity Entity to restore death count
     */
    public static void restoreDeathCount(LivingEntity livingEntity) {
        livingEntity.setData(NarakaAttachments.DEATH_COUNT, DEFAULT_DEATH_COUNT);
    }

    /**
     * Sync death count
     *
     * @param livingEntity Entity to sync death count
     *
     * @see AttachmentSyncHelper#sync(Entity, Supplier, IntAttachmentSyncHandler)
     */
    public static void syncDeathCount(LivingEntity livingEntity) {
        AttachmentSyncHelper.sync(livingEntity, NarakaAttachments.DEATH_COUNT, IntAttachmentSyncHandler.DEATH_COUNT_HANDLER);
    }

    /**
     * Reduce death count<br>
     * if current death count is 1 kill entity<br>
     * Processed when entity die, stigma consumed
     *
     * @param livingEntity Entity to reduce death count
     * @param causingEntity Entity causing death
     *
     * @see NarakaGameEventBus#handleDeathCountOn(LivingDamageEvent)
     * @see StigmaHelper#consumeStigma(LivingEntity, Entity)
     */
    public static void reduceDeathCount(LivingEntity livingEntity, @Nullable Entity causingEntity) {
        int deathCount = getDeathCount(livingEntity);
        if (deathCount == 1)
            livingEntity.hurt(NarakaDamageSources.deathCountZero(causingEntity), Float.MAX_VALUE);
        livingEntity.setData(NarakaAttachments.DEATH_COUNT, deathCount - 1);
        syncDeathCount(livingEntity);
    }
}
