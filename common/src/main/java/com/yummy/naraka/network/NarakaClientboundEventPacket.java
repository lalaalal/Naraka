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

    public enum Event implements StringRepresentable, Runnable {
        START_HEROBRINE_SKY(NarakaClientboundEventHandler::startHerobrineSky),
        STOP_HEROBRINE_SKY(NarakaClientboundEventHandler::stopHerobrineSky),
        START_WHITE_SCREEN(NarakaClientboundEventHandler::startWhiteScreen),
        STOP_WHITE_FOG(NarakaClientboundEventHandler::stopWhiteScreen),
        SHAKE_CAMERA(NarakaClientboundEventHandler::shakeCamera),
        MONOCHROME_EFFECT(NarakaClientboundEventHandler::monochrome),
        RYOIKI_GRAY_EFFECT(NarakaClientboundEventHandler::ryoikiGrayEffect),
        RYOIKI_GREEN_EFFECT(NarakaClientboundEventHandler::ryoikiGreenEffect),
        MUTE_MUSIC_CATEGORY(NarakaClientboundEventHandler::muteMusicCategory),
        FREEZE_TICK(NarakaClientboundEventHandler::freezeTick),
        UNFREEZE_TICK(NarakaClientboundEventHandler::unfreezeTick),
        ;

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        private final Runnable action;

        Event(Runnable action) {
            this.action = action;
        }

        @Override
        public void run() {
            action.run();
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
