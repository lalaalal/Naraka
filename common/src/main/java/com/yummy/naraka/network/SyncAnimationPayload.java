package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record SyncAnimationPayload(int entityId, ResourceLocation animationLocation) implements CustomPacketPayload {
    public static final Type<SyncAnimationPayload> TYPE = new Type<>(NarakaMod.location("sync_animation"));

    public static final StreamCodec<ByteBuf, SyncAnimationPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncAnimationPayload::entityId,
            ResourceLocation.STREAM_CODEC,
            SyncAnimationPayload::animationLocation,
            SyncAnimationPayload::new
    );

    public SyncAnimationPayload(Entity entity, ResourceLocation animationLocation) {
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
