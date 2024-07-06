package com.yummy.naraka.network.payload;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.network.IntAttachmentTypeProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class SyncEntityIntAttachmentPayload extends SyncEntityAttachmentPayload<Integer, IntAttachmentTypeProvider> {
    public static final CustomPacketPayload.Type<SyncEntityIntAttachmentPayload> TYPE = new CustomPacketPayload.Type<>(NarakaMod.location("sync_int_attachment"));
    public static final StreamCodec<ByteBuf, SyncEntityIntAttachmentPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncEntityIntAttachmentPayload::getTargetEntityId,
            ByteBufCodecs.fromCodec(IntAttachmentTypeProvider.CODEC),
            SyncEntityIntAttachmentPayload::getAttachmentTypeProvider,
            ByteBufCodecs.VAR_INT,
            SyncEntityIntAttachmentPayload::getValue,
            SyncEntityIntAttachmentPayload::new
    );

    public SyncEntityIntAttachmentPayload(int targetEntityId, IntAttachmentTypeProvider attachmentTypeProvider, Integer value) {
        super(targetEntityId, attachmentTypeProvider, value);
    }

    public SyncEntityIntAttachmentPayload(Entity target, IntAttachmentTypeProvider attachmentTypeProvider) {
        super(target, attachmentTypeProvider);
    }

    @Override
    protected void send(ServerPlayer player, Entity target) {
        SyncEntityIntAttachmentPayload payload = new SyncEntityIntAttachmentPayload(target, attachmentTypeProvider);
        player.connection.send(payload);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncEntityIntAttachmentPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> payload.synchronize(context.player()));
    }
}
