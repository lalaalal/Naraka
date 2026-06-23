package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record SyncAnimationPacket(int entityId, ResourceLocation animationLocation)
        implements CustomPacketPayload<SyncAnimationPacket> {
    public static final Codec<SyncAnimationPacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("entity_id").forGetter(SyncAnimationPacket::entityId),
                    ResourceLocation.CODEC.fieldOf("animation_location").forGetter(SyncAnimationPacket::animationLocation)
            ).apply(instance, SyncAnimationPacket::new)
    );

    public static final Type<SyncAnimationPacket> TYPE = new CodecType<>(NarakaMod.location("sync_animation"),
            SyncAnimationPacket.class,
            CODEC
    );

    public SyncAnimationPacket(Entity entity, ResourceLocation animationLocation) {
        this(entity.getId(), animationLocation);
    }

    @Override
    public Type<SyncAnimationPacket> type() {
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
