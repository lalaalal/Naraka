package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;

import java.util.function.BiConsumer;

public record NarakaClientboundEventPacket(Event event) implements CustomPacketPayload {
    public static final Type<NarakaClientboundEventPacket> TYPE = new Type<>(NarakaMod.location("clientbound_event_packet"));

    public static final StreamCodec<ByteBuf, NarakaClientboundEventPacket> CODEC = StreamCodec.composite(
            Event.STREAM_CODEC,
            NarakaClientboundEventPacket::event,
            NarakaClientboundEventPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public interface EventHandler {

    }

    public enum Event implements StringRepresentable {
        ;

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.idMapper(Event::byId, Event::getId);

        public final int id;
        public final BiConsumer<EventHandler, NetworkManager.PacketContext> handler;

        Event(BiConsumer<EventHandler, NetworkManager.PacketContext> handler) {
            this.id = ordinal();
            this.handler = handler;
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

        public void handle(EventHandler listener, NetworkManager.PacketContext context) {
            this.handler.accept(listener, context);
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
