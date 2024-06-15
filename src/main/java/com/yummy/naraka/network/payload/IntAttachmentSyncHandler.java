package com.yummy.naraka.network.payload;

import com.yummy.naraka.attachment.NarakaAttachments;
import com.yummy.naraka.event.NarakaCommonEventBus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Handling server, client action when received {@link SyncEntityIntAttachmentPayload}
 *
 * @author lalaalal
 * @see SyncEntityIntAttachmentPayload
 */
public class IntAttachmentSyncHandler {
    public static final IntAttachmentSyncHandler STIGMA_HANDLER = new IntAttachmentSyncHandler(NarakaAttachments.STIGMA);
    public static final IntAttachmentSyncHandler DEATH_COUNT_HANDLER = new IntAttachmentSyncHandler(NarakaAttachments.DEATH_COUNT);
    private static final ArrayList<IntAttachmentSyncHandler> HANDLERS = new ArrayList<>();
    private final AttachmentType<Integer> attachmentType;

    public IntAttachmentSyncHandler(Supplier<AttachmentType<Integer>> supplier) {
        this.attachmentType = supplier.get();
    }

    public static @Nullable IntAttachmentSyncHandler get(int id) {
        return HANDLERS.get(id);
    }

    public static int getId(IntAttachmentSyncHandler handler) {
        return HANDLERS.indexOf(handler);
    }

    private static void add(IntAttachmentSyncHandler handler) {
        HANDLERS.add(handler);
    }

    /**
     * Add all existing {@link IntAttachmentSyncHandler}<br>
     * Called in {@linkplain NarakaCommonEventBus#commonSetup(FMLCommonSetupEvent)}
     *
     * @see NarakaCommonEventBus#commonSetup(FMLCommonSetupEvent)
     */
    public static void initialize() {
        add(STIGMA_HANDLER);
        add(DEATH_COUNT_HANDLER);
    }

    public void handle(Player player, Entity entity, int attachmentValue) {
        if (player.level().isClientSide)
            handleClient(player, entity, attachmentValue);
        else
            handleServer(player, entity, attachmentValue);
    }

    public void handleServer(Player player, Entity entity, int attachmentValue) {
        if (!(entity instanceof LivingEntity livingEntity))
            return;
        if (player instanceof ServerPlayer serverPlayer) {
            int handlerId = getId(this);
            SyncEntityIntAttachmentPayload payload = new SyncEntityIntAttachmentPayload(livingEntity, NarakaAttachments.STIGMA, handlerId);
            serverPlayer.connection.send(payload);
        }
    }

    public void handleClient(Player player, Entity entity, int attachmentValue) {
        if (entity instanceof LivingEntity livingEntity)
            livingEntity.setData(attachmentType, attachmentValue);
    }
}