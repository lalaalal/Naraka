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
    public static final Type<NarakaClientboundEntityEventPacket> TYPE = new Type<>(NarakaMod.location("clientbound_event_packet"));

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

    public interface EventHandler {
        void handle(Entity entity);
    }

    public enum Event implements StringRepresentable {
        ;

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.idMapper(Event::byId, Event::getId);

        public final int id;
        public final EventHandler handler;

        Event(EventHandler handler) {
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

        public void handle(Entity entity) {
            this.handler.handle(entity);
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
