package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public record SkillRequestPayload(int entityId, ResourceLocation location) implements CustomPacketPayload {
    public static final Type<SkillRequestPayload> TYPE = new Type<>(NarakaMod.location("skill_request"));

    public static final StreamCodec<ByteBuf, SkillRequestPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SkillRequestPayload::entityId,
            ResourceLocation.STREAM_CODEC,
            SkillRequestPayload::location,
            SkillRequestPayload::new
    );

    public SkillRequestPayload(SkillUsingMob mob, ResourceLocation location) {
        this(mob.getId(), location);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(NetworkManager.Context context) {
        Level level = context.level();
        Entity entity = level.getEntity(entityId);
        if (entity instanceof SkillUsingMob mob) {
            mob.useSkill(location);
        }
    }
}
