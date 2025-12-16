package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;

public record NarakaClientboundEntityEventPacket(Event event, int entityId) implements CustomPacketPayload {
    public static final Type<NarakaClientboundEntityEventPacket> TYPE = new Type<>(NarakaMod.identifier("clientbound_entity_event_packet"));

    public static final StreamCodec<ByteBuf, NarakaClientboundEntityEventPacket> CODEC = StreamCodec.composite(
            Event.STREAM_CODEC,
            NarakaClientboundEntityEventPacket::event,
            ByteBufCodecs.INT,
            NarakaClientboundEntityEventPacket::entityId,
            NarakaClientboundEntityEventPacket::new
    );

    public NarakaClientboundEntityEventPacket(Event event, Entity entity) {
        this(event, entity.getId());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Event implements StringRepresentable {
        PLAY_HEROBRINE_PHASE_1,
        PLAY_HEROBRINE_PHASE_2,
        PLAY_HEROBRINE_PHASE_3,
        PLAY_HEROBRINE_PHASE_4,
        STOP_MUSIC,
        SHOW_SKILL_CONTROL_SCREEN,
        SHOW_ANIMATION_CONTROL_SCREEN;

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
