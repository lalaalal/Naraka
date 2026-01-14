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
    public static final Type<NarakaClientboundEventPacket> TYPE = new Type<>(NarakaMod.identifier("clientbound_event_packet"));

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
        START_HEROBRINE_SKY,
        STOP_HEROBRINE_SKY,
        START_WHITE_SCREEN,
        STOP_WHITE_FOG,
        SHAKE_CAMERA,
        MONOCHROME_EFFECT,
        RYOIKI_GRAY_EFFECT,
        RYOIKI_GREEN_EFFECT;

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
