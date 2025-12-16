package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record SyncAnimationPacket(int entityId, Identifier animationLocation) implements CustomPacketPayload {
    public static final Type<SyncAnimationPacket> TYPE = new Type<>(NarakaMod.identifier("sync_animation"));

    public static final StreamCodec<ByteBuf, SyncAnimationPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncAnimationPacket::entityId,
            Identifier.STREAM_CODEC,
            SyncAnimationPacket::animationLocation,
            SyncAnimationPacket::new
    );

    public SyncAnimationPacket(Entity entity, Identifier animationLocation) {
        this(entity.getId(), animationLocation);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(NetworkManager.Context context) {
        Player player = context.player();
        Level level = player.level();
        Entity entity = level.getEntity(entityId);
        if (entity instanceof SkillUsingMob mob)
            mob.updateAnimation(animationLocation);
    }
}
