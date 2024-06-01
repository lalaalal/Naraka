package com.yummy.naraka.attachment;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.damagesource.NarakaDamageSources;
import com.yummy.naraka.entity.DeathCountingEntity;
import com.yummy.naraka.event.NarakaGameEventBus;
import com.yummy.naraka.networking.payload.ChangeDeathCountVisibilityPayload;
import com.yummy.naraka.networking.payload.IntAttachmentSyncHandler;
import com.yummy.naraka.tags.NarakaDamageTypeTags;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Help control death count
 *
 * @author lalaalal
 * @see NarakaAttachments#DEATH_COUNT
 */
public class DeathCountHelper {
    private static int maxDeathCount;
    private static final Set<DeathCountingEntity> deathCountingEntities = new HashSet<>();

    public static void loadConfig() {
        maxDeathCount = NarakaMod.config().maxDeathCount.get();
    }

    public static int maxDeathCount() {
        return maxDeathCount;
    }

    /**
     * Add to death counting entities<br>
     * Called in entity constructing event
     *
     * @param deathCountingEntity Entity death counting to add
     */
    public static <T extends LivingEntity & DeathCountingEntity> void addDeathCountingEntity(T deathCountingEntity) {
        if (!deathCountingEntity.level().isClientSide)
            deathCountingEntities.add(deathCountingEntity);
    }

    /**
     * Remove from death counting entities<br>
     * Also call {@link DeathCountHelper#hideDeathCount(ServerPlayer)} for counted player<br>
     * Called in entity death event
     *
     * @param deathCountingEntity Entity counting death count to be removed
     * @see DeathCountHelper#hideDeathCount(ServerPlayer)
     */
    public static <T extends LivingEntity & DeathCountingEntity> void removeDeathCountingEntity(T deathCountingEntity) {
        if (deathCountingEntity.level().isClientSide)
            return;
        deathCountingEntities.remove(deathCountingEntity);
        for (LivingEntity deathCountedEntity : deathCountingEntity.getDeathCountedEntities()) {
            if (deathCountedEntity instanceof ServerPlayer serverPlayer)
                hideDeathCount(serverPlayer);
        }
    }

    public static void showDeathCount(ServerPlayer player) {
        player.connection.send(
                new ChangeDeathCountVisibilityPayload(true)
        );
    }

    /**
     * Send {@linkplain ChangeDeathCountVisibilityPayload} to hide player death count<br>
     * Skip if other counting entity exists
     *
     * @param player Player to hide death count
     * @see DeathCountHelper#removeDeathCountingEntity(LivingEntity)
     */
    public static void hideDeathCount(ServerPlayer player) {
        for (DeathCountingEntity deathCountingEntity : deathCountingEntities) {
            if (deathCountingEntity.getDeathCountedEntities().contains(player))
                return;
        }
        player.connection.send(
                new ChangeDeathCountVisibilityPayload(false)
        );
    }

    public static void updateDeathCountVisibility(ServerPlayer player) {
        if (isDeathCounted(player))
            showDeathCount(player);
        else
            hideDeathCount(player);
    }

    public static boolean isDeathCounted(LivingEntity livingEntity) {
        for (DeathCountingEntity deathCountingEntity : deathCountingEntities) {
            if (deathCountingEntity.getDeathCountedEntities().contains(livingEntity))
                return true;
        }
        return false;
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
        if (livingEntity instanceof ServerPlayer serverPlayer)
            updateDeathCountVisibility(serverPlayer);
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
