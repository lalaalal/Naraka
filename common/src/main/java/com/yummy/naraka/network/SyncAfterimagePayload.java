package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public record SyncAfterimagePayload(int entityId, Afterimage afterimage) implements CustomPacketPayload {
    public static final Type<SyncAfterimagePayload> TYPE = new Type<>(NarakaMod.location("sync_afterimage_payload"));

    public static final StreamCodec<ByteBuf, SyncAfterimagePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncAfterimagePayload::entityId,
            Afterimage.STREAM_CODEC,
            SyncAfterimagePayload::afterimage,
            SyncAfterimagePayload::new
    );

    public SyncAfterimagePayload(Entity entity, Afterimage afterimage) {
        this(entity.getId(), afterimage);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(NetworkManager.PacketContext context) {
        Level level = context.getPlayer().level();
        Entity entity = level.getEntity(entityId);

        if (entity instanceof AfterimageEntity afterimageEntity)
            afterimageEntity.addAfterimage(afterimage);
    }
}
