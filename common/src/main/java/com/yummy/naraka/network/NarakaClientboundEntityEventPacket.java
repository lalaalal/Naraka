package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;

public record NarakaClientboundEntityEventPacket(Event event, int entityId)
        implements CustomPacketPayload<NarakaClientboundEntityEventPacket> {
    public static final Codec<NarakaClientboundEntityEventPacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Event.CODEC.fieldOf("event").forGetter(NarakaClientboundEntityEventPacket::event),
                    Codec.INT.fieldOf("entityId").forGetter(NarakaClientboundEntityEventPacket::entityId)
            ).apply(instance, NarakaClientboundEntityEventPacket::new)
    );

    public static final Type<NarakaClientboundEntityEventPacket> TYPE = new CodecType<>(
            NarakaMod.location("clientbound_entity_event_packet"),
            NarakaClientboundEntityEventPacket.class,
            CODEC
    );

    public NarakaClientboundEntityEventPacket(Event event, Entity entity) {
        this(event, entity.getId());
    }

    @Override
    public Type<NarakaClientboundEntityEventPacket> type() {
        return TYPE;
    }

    public enum Event implements StringRepresentable {
        PLAY_HEROBRINE_PHASE_1,
        PLAY_HEROBRINE_PHASE_2,
        PLAY_HEROBRINE_PHASE_3,
        PLAY_HEROBRINE_PHASE_4,
        STOP_BOSS_MUSIC,
        SHOW_SKILL_CONTROL_SCREEN,
        SHOW_ANIMATION_CONTROL_SCREEN;

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
