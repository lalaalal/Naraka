package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public record NarakaClientboundEventPacket(List<Event> events)
        implements CustomPacketPayload<NarakaClientboundEventPacket> {
    public static final Codec<NarakaClientboundEventPacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Event.CODEC.listOf().fieldOf("events").forGetter(NarakaClientboundEventPacket::events)
            ).apply(instance, NarakaClientboundEventPacket::new)
    );

    public static final Type<NarakaClientboundEventPacket> TYPE = new CodecType<>(
            NarakaMod.location("clientbound_event_packet"),
            NarakaClientboundEventPacket.class,
            CODEC
    );

    public NarakaClientboundEventPacket(Event... events) {
        this(List.of(events));
    }

    @Override
    public Type<NarakaClientboundEventPacket> type() {
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
        RYOIKI_GREEN_EFFECT,
        MUTE_MUSIC_CATEGORY;

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
