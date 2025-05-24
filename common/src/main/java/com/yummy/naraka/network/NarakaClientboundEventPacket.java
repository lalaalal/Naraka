package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public record NarakaClientboundEventPacket(List<Event> events) implements CustomPacketPayload {
    public static final Type<NarakaClientboundEventPacket> TYPE = new Type<>(NarakaMod.location("clientbound_event_packet"));

    public static final StreamCodec<ByteBuf, NarakaClientboundEventPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.<ByteBuf, Event>list().apply(Event.STREAM_CODEC),
            NarakaClientboundEventPacket::events,
            NarakaClientboundEventPacket::new
    );

    public NarakaClientboundEventPacket(Event... events) {
        this(List.of(events));
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
        START_HEROBRINE_SKY,
        STOP_HEROBRINE_SKY;

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.idMapper(Event::byId, Event::getId);

        public final int id;

        Event() {
            this.id = ordinal();
        }

        public static Event byId(int id) {
            for (Event event : values()) {
                if (event.id == id)
                    return event;
            }
            throw new IllegalArgumentException("Unknown event id: " + id);
        }

        public int getId() {
            return id;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
