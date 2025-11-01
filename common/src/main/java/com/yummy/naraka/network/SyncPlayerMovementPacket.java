package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

public record SyncPlayerMovementPacket(Vec3 movement) implements CustomPacketPayload {
    public static final Type<SyncPlayerMovementPacket> TYPE = new Type<>(NarakaMod.location("sync_player_movement"));

    public static final StreamCodec<ByteBuf, SyncPlayerMovementPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Vec3.CODEC),
            SyncPlayerMovementPacket::movement,
            SyncPlayerMovementPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(NetworkManager.Context context) {
        context.player().setDeltaMovement(movement);
    }
}
