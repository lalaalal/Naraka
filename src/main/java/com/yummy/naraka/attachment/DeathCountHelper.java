package com.yummy.naraka.attachment;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.event.NarakaGameEventBus;
import com.yummy.naraka.network.IntAttachmentTypeProvider;
import com.yummy.naraka.network.payload.ChangeDeathCountVisibilityPayload;
import com.yummy.naraka.tags.NarakaDamageTypeTags;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.util.NarakaUtil;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.DeathCountingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Help control death count
 *
 * @author lalaalal
 * @see NarakaAttachments#DEATH_COUNT
 */
public class DeathCountHelper {
    private static int maxDeathCount;
    private static final Set<UUID> uuids = new HashSet<>();
    private static final Set<DeathCountingEntity> cache = new HashSet<>();

    public static void loadConfig() {
        maxDeathCount = NarakaMod.config().maxDeathCount.get();
    }

    public static int maxDeathCount() {
        return maxDeathCount;
    }

    private static Set<DeathCountingEntity> getDeathCountingEntities() {
        if (uuids.size() != cache.size()) {
            cache.clear();
            for (UUID uuid : uuids) {
                DeathCountingEntity deathCountingEntity = NarakaUtil.findEntityByUUID(uuid, DeathCountingEntity.class);
                if (deathCountingEntity != null)
                    cache.add(deathCountingEntity);
            }
        }
        return cache;
    }

    /**
     * Add to death counting entities<br>
     * Called in entity constructing event
     *
     * @param deathCountingEntity Entity death counting to add
     */
    public static <T extends LivingEntity & DeathCountingEntity> void addDeathCountingEntity(T deathCountingEntity) {
        if (deathCountingEntity.level().isClientSide)
            return;
        uuids.add(deathCountingEntity.getUUID());
        for (UUID uuid : deathCountingEntity.getDeathCountedEntities()) {
            Entity deathCountedEntity = NarakaUtil.findEntityByUUID(uuid);
            if (deathCountedEntity instanceof ServerPlayer serverPlayer)
                showDeathCount(serverPlayer);
        }
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
        uuids.remove(deathCountingEntity.getUUID());
        for (UUID uuid : deathCountingEntity.getDeathCountedEntities()) {
            Entity deathCountedEntity = NarakaUtil.findEntityByUUID(uuid);
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
        for (DeathCountingEntity deathCountingEntity : getDeathCountingEntities()) {
            if (deathCountingEntity.isDeathCounting(player))
                return;
        }
        player.connection.send(
                new ChangeDeathCountVisibilityPayload(false)
        );
    }

    /**
     * Sync all death counted entities by counting entity
     *
     * @param deathCountingEntity Death counting entity
     * @param <T>                 DeathCountingEntity
     */
    public static <T extends Entity & DeathCountingEntity> void updateDeathCountingEntity(T deathCountingEntity) {
        if (deathCountingEntity.level().isClientSide)
            return;
        uuids.add(deathCountingEntity.getUUID());
        for (UUID uuid : deathCountingEntity.getDeathCountedEntities()) {
            LivingEntity deathCountedEntity = NarakaUtil.findEntityByUUID(uuid, LivingEntity.class);
            if (deathCountedEntity != null)
                syncDeathCount(deathCountedEntity);
        }
    }

    public static void updateDeathCountVisibility(ServerPlayer player) {
        if (isDeathCounted(player))
            showDeathCount(player);
        else
            hideDeathCount(player);
    }

    public static boolean isDeathCounted(LivingEntity livingEntity) {
        if (livingEntity.level().isClientSide)
            return false;
        for (DeathCountingEntity deathCountingEntity : getDeathCountingEntities()) {
            if (deathCountingEntity.isDeathCounting(livingEntity))
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
        syncDeathCount(livingEntity);
    }

    /**
     * Sync death count
     *
     * @param livingEntity Entity to sync death count
     * @see AttachmentSyncHelper#sync(Entity, IntAttachmentTypeProvider)
     */
    public static void syncDeathCount(LivingEntity livingEntity) {
        AttachmentSyncHelper.sync(livingEntity, IntAttachmentTypeProvider.DEATH_COUNT);
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
     * @see NarakaGameEventBus#handleDeathCountOn(LivingDamageEvent.Pre)
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
