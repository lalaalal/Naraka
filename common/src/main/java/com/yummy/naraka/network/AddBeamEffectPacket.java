package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.BeamEffectsHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.BiConsumer;

public record AddBeamEffectPacket(int entityId, BeamEffectType beamEffectType,
                                  int color) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AddBeamEffectPacket> TYPE = new Type<>(NarakaMod.location("add_beam_effect"));

    public static final StreamCodec<ByteBuf, AddBeamEffectPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            AddBeamEffectPacket::entityId,
            BeamEffectType.STREAM_CODEC,
            AddBeamEffectPacket::beamEffectType,
            ByteBufCodecs.INT,
            AddBeamEffectPacket::color,
            AddBeamEffectPacket::new
    );

    public AddBeamEffectPacket(BeamEffectType beamEffectType, LivingEntity livingEntity, int color) {
        this(livingEntity.getId(), beamEffectType, color);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private Optional<LivingEntity> getEntity(Level level) {
        Entity entity = level.getEntity(entityId);
        if (entity instanceof LivingEntity livingEntity)
            return Optional.of(livingEntity);
        return Optional.empty();
    }

    public void handle(NetworkManager.Context context) {
        getEntity(context.level()).ifPresent(
                livingEntity -> beamEffectType.effectAdder.accept(livingEntity, color)
        );
    }

    public enum BeamEffectType implements StringRepresentable {
        SIMPLE(BeamEffectsHelper::addSimpleSet),
        PULL(BeamEffectsHelper::addPullSet),
        PUSH(BeamEffectsHelper::addPushSet);

        public static final Codec<BeamEffectType> CODEC = StringRepresentable.fromEnum(BeamEffectType::values);
        public static final StreamCodec<ByteBuf, BeamEffectType> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        private final BiConsumer<LivingEntity, Integer> effectAdder;

        BeamEffectType(BiConsumer<LivingEntity, Integer> effectAdder) {
            this.effectAdder = effectAdder;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
