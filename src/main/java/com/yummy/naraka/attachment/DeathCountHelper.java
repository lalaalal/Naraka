package com.yummy.naraka.attachment;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.damagesource.NarakaDamageSources;
import com.yummy.naraka.event.NarakaGameEventBus;
import com.yummy.naraka.networking.payload.IntAttachmentSyncHandler;
import com.yummy.naraka.tags.NarakaDamageTypeTags;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Help control death count
 *
 * @author lalaalal
 * @see NarakaAttachments#DEATH_COUNT
 */
public class DeathCountHelper {
    private static int maxDeathCount = NarakaMod.config().MAX_DEATH_COUNT.get();

    public static void loadConfig() {
        maxDeathCount = NarakaMod.config().MAX_DEATH_COUNT.get();
    }

    public static int maxDeathCount() {
        return maxDeathCount;
    }

    public static boolean isDeathCountingAttack(DamageSource source) {
        Entity cause = source.getEntity();
        if (cause != null && cause.getType().is(NarakaEntityTypeTags.DEATH_COUNTING_ENTITY))
            return true;

        return source.is(NarakaDamageTypeTags.DEATH_COUNTING_ATTACK);
    }

    public static int getDeathCount(LivingEntity livingEntity) {
        return livingEntity.getData(NarakaAttachments.DEATH_COUNT);
    }

    /**
     * Restore death count to {@linkplain DeathCountHelper#maxDeathCount()}
     *
     * @param livingEntity Entity to restore death count
     */
    public static void restoreDeathCount(LivingEntity livingEntity) {
        livingEntity.setData(NarakaAttachments.DEATH_COUNT, maxDeathCount);
    }

    /**
     * Sync death count
     *
     * @param livingEntity Entity to sync death count
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
     * @param livingEntity  Entity to reduce death count
     * @param causingEntity Entity causing death
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
