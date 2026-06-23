package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.BeamEffectsHelper;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;

import java.util.function.BiConsumer;

public record AddBeamEffectPacket(int entityId, BeamEffectType beamEffectType, int color)
        implements CustomPacketPayload<AddBeamEffectPacket> {
    public static final Codec<AddBeamEffectPacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("entity_id").forGetter(AddBeamEffectPacket::entityId),
                    BeamEffectType.CODEC.fieldOf("beam_effect_type").forGetter(AddBeamEffectPacket::beamEffectType),
                    Codec.INT.fieldOf("color").forGetter(AddBeamEffectPacket::color)
            ).apply(instance, AddBeamEffectPacket::new)
    );

    public static final CustomPacketPayload.Type<AddBeamEffectPacket> TYPE = new CodecType<>(NarakaMod.location("add_beam_effect"),
            AddBeamEffectPacket.class,
            CODEC
    );

    public AddBeamEffectPacket(BeamEffectType beamEffectType, Entity entity, int color) {
        this(entity.getId(), beamEffectType, color);
    }

    @Override
    public Type<AddBeamEffectPacket> type() {
        return TYPE;
    }

    public void handle(NetworkManager.Context context) {
        Entity entity = context.level().getEntity(entityId);
        if (entity != null)
            beamEffectType.effectAdder.accept(entity, color);
    }

    public enum BeamEffectType implements StringRepresentable {
        SIMPLE(BeamEffectsHelper::addSimpleSet),
        PULL(BeamEffectsHelper::addPullSet),
        PUSH(BeamEffectsHelper::addPushSet);

        public static final Codec<BeamEffectType> CODEC = StringRepresentable.fromEnum(BeamEffectType::values);

        private final BiConsumer<Entity, Integer> effectAdder;

        BeamEffectType(BiConsumer<Entity, Integer> effectAdder) {
            this.effectAdder = effectAdder;
        }

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}
