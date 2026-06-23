package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public record SyncPlayerMovementPacket(Vec3 movement) implements CustomPacketPayload<SyncPlayerMovementPacket> {
    public static final Type<SyncPlayerMovementPacket> TYPE = new SimpleType<>(NarakaMod.location("sync_player_movement"),
            SyncPlayerMovementPacket.class,
            SyncPlayerMovementPacket::encode,
            SyncPlayerMovementPacket::decode
    );

    private static SyncPlayerMovementPacket decode(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new SyncPlayerMovementPacket(new Vec3(x, y, z));
    }

    @Override
    public Type<SyncPlayerMovementPacket> type() {
        return TYPE;
    }

    private void encode(FriendlyByteBuf buffer) {
        buffer.writeDouble(movement.x);
        buffer.writeDouble(movement.y);
        buffer.writeDouble(movement.z);
    }

    public void handle(NetworkManager.Context context) {
        context.player().setDeltaMovement(movement);
    }
}
