package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record SyncAnimationPayload(int entityId, String animationName) implements CustomPacketPayload {
    public static final Type<SyncAnimationPayload> TYPE = new Type<>(NarakaMod.location("sync_animation"));

    public static final StreamCodec<ByteBuf, SyncAnimationPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncAnimationPayload::entityId,
            ByteBufCodecs.STRING_UTF8,
            SyncAnimationPayload::animationName,
            SyncAnimationPayload::new
    );

    public SyncAnimationPayload(Entity entity, String animationName) {
        this(entity.getId(), animationName);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        Level level = player.level();
        Entity entity = level.getEntity(entityId);
        if (entity instanceof SkillUsingMob mob)
            mob.updateAnimation(animationName);
    }
}
