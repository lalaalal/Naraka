package com.yummy.naraka.network.payload;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.attachment.AttachmentSynchronizer;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class SyncEntityIntAttachmentPayload extends SyncEntityAttachmentPayload<Integer> {
    public static final CustomPacketPayload.Type<SyncEntityIntAttachmentPayload> TYPE = new CustomPacketPayload.Type<>(NarakaMod.location("sync_int_attachment"));
    public static final StreamCodec<ByteBuf, SyncEntityIntAttachmentPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncEntityIntAttachmentPayload::getTargetEntityId,
            ByteBufCodecs.VAR_INT,
            SyncEntityIntAttachmentPayload::getValue,
            ByteBufCodecs.fromCodec(AttachmentSynchronizer.CODEC),
            SyncEntityIntAttachmentPayload::getSynchronizer,
            SyncEntityIntAttachmentPayload::new
    );

    public SyncEntityIntAttachmentPayload(int targetEntityId, Integer value, Holder<AttachmentSynchronizer> synchronizer) {
        super(targetEntityId, value, synchronizer);
    }

    public SyncEntityIntAttachmentPayload(Entity targetEntity, Integer value, Holder<AttachmentSynchronizer> synchronizer) {
        super(targetEntity, value, synchronizer);
    }

    public static void handle(SyncEntityIntAttachmentPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> payload.synchronize(context.player()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
