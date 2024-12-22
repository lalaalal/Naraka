package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;

import java.util.function.Consumer;

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
        @Deprecated
        void handleDeathCountUsed();
    }

    public enum Event implements StringRepresentable {
        DEATH_COUNT_USED(EventHandler::handleDeathCountUsed);

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.idMapper(Event::byId, Event::getId);

        public final int id;
        public final Consumer<EventHandler> handler;

        Event(Consumer<EventHandler> handler) {
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

        public void handle(EventHandler listener) {
            this.handler.accept(listener);
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
