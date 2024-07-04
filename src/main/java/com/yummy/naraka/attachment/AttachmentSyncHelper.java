package com.yummy.naraka.attachment;

import com.yummy.naraka.event.NarakaGameEventBus;
import com.yummy.naraka.network.payload.IntAttachmentSyncHandler;
import com.yummy.naraka.network.payload.SyncEntityIntAttachmentPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Helper for syncing attachment<br>
 * Attachments need to sync on player logged in
 *
 * @author lalaalal
 * @see NarakaGameEventBus#syncAttachmentsOn(PlayerEvent.PlayerLoggedInEvent)
 */
public class AttachmentSyncHelper {
    private static final List<Consumer<LivingEntity>> attachmentSyncers = new ArrayList<>();

    public static void initialize() {
        attachmentSyncers.add(StigmaHelper::syncStigma);
        attachmentSyncers.add(DeathCountHelper::syncDeathCount);
    }

    /**
     * Sync entity's attachment value both server and client
     * Using {@link SyncEntityIntAttachmentPayload} to sync
     *
     * <ul>
     *     <li>In server : notice to all players</li>
     *     <li>In Client : request server to notice</li>
     * </ul>
     *
     * @param entity   Entity having attachment
     * @param supplier Supplier of {@link AttachmentType}
     * @param handler  Handler {@link SyncEntityIntAttachmentPayload} using
     * @see SyncEntityIntAttachmentPayload
     */
    public static void sync(Entity entity, Supplier<AttachmentType<Integer>> supplier, Holder<AttachmentSynchronizer> synchronizer) {
        int value = entity.getData(supplier.get());
        if (entity.level() instanceof ServerLevel serverLevel) {
            
            SyncEntityIntAttachmentPayload payload = new SyncEntityIntAttachmentPayload(entity, value, synchronizer);
            for (ServerPlayer player : serverLevel.players()) {
                player.connection.send(payload);
            }
        }
        if (entity.level().isClientSide) {
            SyncEntityIntAttachmentPayload request = new SyncEntityIntAttachmentPayload(entity, value, synchronizer);
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null)
                return;
            localPlayer.connection.send(request);
        }
    }

    /**
     * Sync all attachments using syncer
     *
     * @param livingEntity Entity to sync
     * @see AttachmentSyncHelper#initialize()
     */
    public static void syncAttachments(LivingEntity livingEntity) {
        for (Consumer<LivingEntity> syncer : attachmentSyncers)
            syncer.accept(livingEntity);
    }
}
