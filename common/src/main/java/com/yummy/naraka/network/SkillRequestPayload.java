package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public record SkillRequestPayload(Event event, int entityId, ResourceLocation location) implements CustomPacketPayload {
    public static final Type<SkillRequestPayload> TYPE = new Type<>(NarakaMod.location("skill_request"));

    public static final StreamCodec<ByteBuf, SkillRequestPayload> CODEC = StreamCodec.composite(
            Event.STREAM_CODEC,
            SkillRequestPayload::event,
            ByteBufCodecs.INT,
            SkillRequestPayload::entityId,
            ResourceLocation.STREAM_CODEC,
            SkillRequestPayload::location,
            SkillRequestPayload::new
    );

    public SkillRequestPayload(Event event, SkillUsingMob mob, ResourceLocation location) {
        this(event, mob.getId(), location);
    }

    public SkillRequestPayload(Event event, SkillUsingMob mob) {
        this(event, mob.getId(), NarakaMod.location("empty"));
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

    public enum Event {
        DISABLE((payload, context) -> {
            payload.getEntity(context.level()).ifPresent(mob -> {
                mob.getSkillManager().enableOnly(List.of());
            });
        }),
        STOP((payload, context) -> {
            payload.getEntity(context.level()).ifPresent(mob -> {
                mob.getSkillManager().interrupt();
            });
        }),
        ENABLE_ONLY((payload, context) -> {
            payload.getEntity(context.level()).ifPresent(mob -> {
                Skill<?> skill = mob.getSkillManager().getSkill(payload.location);
                if (skill != null)
                    mob.getSkillManager().enableOnly(List.of(skill));
            });
        }),
        USE((payload, context) -> {
            payload.getEntity(context.level()).ifPresent(mob -> mob.useSkill(payload.location));
        });

        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.idMapper(Event::byId, Event::ordinal);

        public final NetworkManager.PacketHandler<SkillRequestPayload> handler;

        Event(NetworkManager.PacketHandler<SkillRequestPayload> handler) {
            this.handler = handler;
        }

        public static Event byId(int id) {
            for (Event event : values()) {
                if (event.ordinal() == id)
                    return event;
            }
            throw new IllegalArgumentException("Unknown event id: " + id);
        }
    }
}
