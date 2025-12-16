package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public record SkillRequestPacket(Event event, int entityId, Identifier location) implements CustomPacketPayload {
    public static final Type<SkillRequestPacket> TYPE = new Type<>(NarakaMod.identifier("skill_request"));

    public static final StreamCodec<ByteBuf, SkillRequestPacket> CODEC = StreamCodec.composite(
            Event.STREAM_CODEC,
            SkillRequestPacket::event,
            ByteBufCodecs.INT,
            SkillRequestPacket::entityId,
            Identifier.STREAM_CODEC,
            SkillRequestPacket::location,
            SkillRequestPacket::new
    );

    public SkillRequestPacket(Event event, SkillUsingMob mob, Identifier location) {
        this(event, mob.getId(), location);
    }

    public SkillRequestPacket(Event event, SkillUsingMob mob) {
        this(event, mob.getId(), NarakaMod.identifier("empty"));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private Optional<SkillUsingMob> getEntity(Level level) {
        Entity entity = level.getEntity(entityId);
        if (entity instanceof SkillUsingMob skillUsingMob)
            return Optional.of(skillUsingMob);
        return Optional.empty();
    }

    public void handle(NetworkManager.Context context) {
        event.handler.handle(this, context);
    }

    private static void disable(SkillRequestPacket packet, NetworkManager.Context context) {
        packet.getEntity(context.level()).ifPresent(mob -> {
            mob.getSkillManager().enableOnly(List.of());
        });
    }

    private static void stop(SkillRequestPacket packet, NetworkManager.Context context) {
        packet.getEntity(context.level()).ifPresent(mob -> {
            mob.getSkillManager().interrupt();
        });
    }

    private static void enableOnly(SkillRequestPacket packet, NetworkManager.Context context) {
        packet.getEntity(context.level()).ifPresent(mob -> {
            Skill<?> skill = mob.getSkillManager().getSkill(packet.location);
            if (skill != null)
                mob.getSkillManager().enableOnly(List.of(skill));
        });
    }

    private static void use(SkillRequestPacket packet, NetworkManager.Context context) {
        packet.getEntity(context.level()).ifPresent(mob -> mob.useSkill(packet.location));
    }

    public enum Event implements StringRepresentable {
        DISABLE(SkillRequestPacket::disable),
        STOP(SkillRequestPacket::stop),
        ENABLE_ONLY(SkillRequestPacket::enableOnly),
        USE(SkillRequestPacket::use);

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        public final NetworkManager.PacketHandler<SkillRequestPacket> handler;

        Event(NetworkManager.PacketHandler<SkillRequestPacket> handler) {
            this.handler = handler;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
