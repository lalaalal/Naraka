package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record SyncEntityMotionPacket(
        int entityId,
        Vec3 position,
        Vec3 movement,
        float yRot,
        float xRot
) implements CustomPacketPayload<SyncEntityMotionPacket> {
    public static final Codec<SyncEntityMotionPacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("entity_id").forGetter(SyncEntityMotionPacket::entityId),
                    Vec3.CODEC.fieldOf("position").forGetter(SyncEntityMotionPacket::position),
                    Vec3.CODEC.fieldOf("movement").forGetter(SyncEntityMotionPacket::movement),
                    Codec.FLOAT.fieldOf("yRot").forGetter(SyncEntityMotionPacket::yRot),
                    Codec.FLOAT.fieldOf("xRot").forGetter(SyncEntityMotionPacket::xRot)
            ).apply(instance, SyncEntityMotionPacket::new)
    );

    public static final Type<SyncEntityMotionPacket> TYPE = new CodecType<>(
            NarakaMod.location("sync_entity_motion"),
            SyncEntityMotionPacket.class,
            CODEC
    );

    public static SyncEntityMotionPacket of(Entity entity) {
        return new SyncEntityMotionPacket(entity.getId(), entity.position(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
    }

    public static void handle(SyncEntityMotionPacket packet, NetworkManager.Context context) {
        Entity entity = context.level().getEntity(packet.entityId);
        if (entity != null) {
            entity.lerpTo(packet.position.x, packet.position.y, packet.position.z, packet.yRot, packet.xRot, 3, false);
        }
    }

    @Override
    public Type<SyncEntityMotionPacket> type() {
        return TYPE;
    }
}
