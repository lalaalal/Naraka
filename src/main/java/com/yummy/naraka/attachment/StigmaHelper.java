package com.yummy.naraka.attachment;

import com.yummy.naraka.NarakaConfig;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.event.NarakaGameEventBus;
import com.yummy.naraka.network.payload.IntAttachmentSyncHandler;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Help control Stigma
 *
 * @author lalaalal
 * @see NarakaAttachments#STIGMA
 */
public class StigmaHelper {
    private static final Map<LivingEntity, Long> stigmaTimestamps = new HashMap<>();
    private static int maxStigma;
    /**
     * Ticks to pause entity
     *
     * @see StigmaHelper#shouldPauseEntity(LivingEntity)
     */
    private static int pauseDuration;
    /**
     * Ticks to keep stigma<br>
     * Reduce after given ticks
     *
     * @see StigmaHelper#handleSulliedEntity(LivingEntity)
     */
    private static int keepStigmaDuration;

    public static void loadConfig() {
        NarakaConfig config = NarakaMod.config();
        maxStigma = config.maxStigma.get();
        pauseDuration = config.pauseDurationByStigma.get();
        keepStigmaDuration = config.keepStigmaDuration.get();
    }

    /**
     * Get value of a LivingEntity
     *
     * @param livingEntity Entity to get value
     * @return Stigma value of livingEntity
     */
    public static int getStigma(LivingEntity livingEntity) {
        return livingEntity.getData(NarakaAttachments.STIGMA);
    }

    /**
     * Returns base value of entity's max health attribute
     *
     * @param livingEntity Entity to get max health
     * @return Original MaxHealth
     */
    public static float getOriginalMaxHealth(LivingEntity livingEntity) {
        AttributeInstance instance = livingEntity.getAttribute(Attributes.MAX_HEALTH);
        if (instance == null)
            return 0;
        return (float) instance.getBaseValue();
    }

    /**
     * Sync stigma
     *
     * @param livingEntity Entity to sync
     * @see AttachmentSyncHelper#sync(Entity, Supplier, IntAttachmentSyncHandler)
     */
    public static void syncStigma(LivingEntity livingEntity) {
        AttachmentSyncHelper.sync(livingEntity, NarakaAttachments.STIGMA, NarakaAttachmentSynchronizers.STIGMA_SYNCHRONIZER);
    }

    /**
     * Increase value of LivingEntity
     * If value == {@link StigmaHelper#maxStigma} <br>
     * call {@link StigmaHelper#consumeStigma(LivingEntity, Entity)}
     *
     * @param livingEntity  Entity to increase value
     * @param causingEntity Entity caused stigma
     */
    public static void increaseStigma(LivingEntity livingEntity, @Nullable Entity causingEntity) {
        int stigma = livingEntity.getData(NarakaAttachments.STIGMA);
        livingEntity.setData(NarakaAttachments.STIGMA, stigma + 1);
        stigmaTimestamps.put(livingEntity, livingEntity.level().getGameTime());
        if (stigma + 1 == maxStigma)
            consumeStigma(livingEntity, causingEntity);
        syncStigma(livingEntity);
    }

    /**
     * @see StigmaHelper#increaseStigma(LivingEntity, Entity)
     */
    public static void increaseStigma(LivingEntity livingEntity) {
        increaseStigma(livingEntity, null);
    }

    public static boolean shouldPauseEntity(LivingEntity livingEntity) {
        return !isTickPassed(livingEntity, pauseDuration);
    }

    public static boolean canDecreaseStigma(LivingEntity livingEntity) {
        return isTickPassed(livingEntity, keepStigmaDuration);
    }

    private static boolean isTickPassed(LivingEntity livingEntity, int tickDuration) {
        if (!stigmaTimestamps.containsKey(livingEntity))
            return false;
        long timestamp = stigmaTimestamps.get(livingEntity);
        long current = livingEntity.level().getGameTime();
        return current - timestamp > tickDuration;
    }

    /**
     * Handle entity that is sullied stigma<br>
     * Pause if entity just sullied or resume
     *
     * @param livingEntity Entity to handle
     * @see StigmaHelper#shouldPauseEntity(LivingEntity)
     * @see StigmaHelper#pauseEntity(LivingEntity)
     * @see StigmaHelper#resumeEntity(LivingEntity)
     * @see NarakaGameEventBus#handleSulliedEntityOn(EntityTickEvent.Pre)
     */
    public static void handleSulliedEntity(LivingEntity livingEntity) {
        if (getStigma(livingEntity) <= 0)
            return;
        if (!stigmaTimestamps.containsKey(livingEntity))
            stigmaTimestamps.put(livingEntity, livingEntity.level().getGameTime());
        if (shouldPauseEntity(livingEntity))
            pauseEntity(livingEntity);
        else {
            resumeEntity(livingEntity);
            if (canDecreaseStigma(livingEntity)) {
                decreaseStigma(livingEntity);
                stigmaTimestamps.remove(livingEntity);
            }
        }
    }

    /**
     * Block moving and jumping entity
     *
     * @param livingEntity Entity to pause
     * @see StigmaHelper#handleSulliedEntity(LivingEntity)
     */
    public static void pauseEntity(LivingEntity livingEntity) {
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.MOVEMENT_SPEED,
                NarakaAttributeModifiers.BLOCK_MOVING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.JUMP_STRENGTH,
                NarakaAttributeModifiers.BLOCK_JUMPING
        );

        if (livingEntity instanceof Mob mob)
            mob.setNoAi(true);
    }

    public static void resumeEntity(LivingEntity livingEntity) {
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(false);
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.MOVEMENT_SPEED,
                NarakaAttributeModifiers.BLOCK_MOVING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.JUMP_STRENGTH,
                NarakaAttributeModifiers.BLOCK_JUMPING
        );
    }

    /**
     * Make stigma to 0 and reduce death count
     * Calling {@link StigmaHelper#syncStigma(LivingEntity)} is required after use
     *
     * @param livingEntity  Entity to consume value
     * @param causingEntity Entity caused stigma
     * @see DeathCountHelper#reduceDeathCount(LivingEntity, Entity)
     */
    public static void consumeStigma(LivingEntity livingEntity, @Nullable Entity causingEntity) {
        livingEntity.setData(NarakaAttachments.STIGMA, 0);
        if (DeathCountHelper.isDeathCounted(livingEntity))
            DeathCountHelper.reduceDeathCount(livingEntity, causingEntity);
    }

    public static void decreaseStigma(LivingEntity livingEntity) {
        int stigma = livingEntity.getData(NarakaAttachments.STIGMA);
        if (stigma > 0)
            livingEntity.setData(NarakaAttachments.STIGMA, stigma - 1);
        syncStigma(livingEntity);
    }
}
