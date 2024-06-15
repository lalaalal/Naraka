package com.yummy.naraka.network.payload;

import com.yummy.naraka.NarakaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

/**
 * Payload for syncing integer attachment
 * Use {@link SyncEntityIntAttachmentPayload#SyncEntityIntAttachmentPayload(Entity, Supplier, int)}
 *
 * @param entityId
 * @param value
 * @param handlerId
 * @author lalaalal
 */
public record SyncEntityIntAttachmentPayload(int entityId, int value, int handlerId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncEntityIntAttachmentPayload> TYPE = new CustomPacketPayload.Type<>(NarakaMod.location("sync_stigma"));
    public static final StreamCodec<ByteBuf, SyncEntityIntAttachmentPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncEntityIntAttachmentPayload::entityId,
            ByteBufCodecs.VAR_INT,
            SyncEntityIntAttachmentPayload::value,
            ByteBufCodecs.VAR_INT,
            SyncEntityIntAttachmentPayload::handlerId,
            SyncEntityIntAttachmentPayload::new
    );

    /**
     * Construct {@linkplain SyncEntityIntAttachmentPayload}
     *
     * @param entity    Entity having attachment
     * @param supplier  Supplier of {@link AttachmentType}
     * @param handlerId Handler id (use {@link IntAttachmentSyncHandler#getId(IntAttachmentSyncHandler)} to gei id of existing handler)
     * @see IntAttachmentSyncHandler
     */
    public SyncEntityIntAttachmentPayload(Entity entity, Supplier<AttachmentType<Integer>> supplier, int handlerId) {
        this(entity.getId(), entity.getData(supplier), handlerId);
    }

    public static void handle(SyncEntityIntAttachmentPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            IntAttachmentSyncHandler handler = IntAttachmentSyncHandler.get(payload.handlerId);
            if (handler == null)
                return;
            Player player = context.player();
            Entity entity = player.level().getEntity(payload.entityId);
            if (entity != null)
                handler.handle(player, entity, payload.value);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
