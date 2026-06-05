package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.overlay.ProgressOverlayData;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.BossEvent;

import java.util.Optional;
import java.util.UUID;

public record SyncProgressOverlayExtensionPacket(UUID uuid, Action action,
                                                 Optional<ProgressOverlayData<?>> data) implements CustomPacketPayload {
    public static final Type<SyncProgressOverlayExtensionPacket> TYPE = new Type<>(NarakaMod.identifier("sync_progress_overlay_extension"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncProgressOverlayExtensionPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            SyncProgressOverlayExtensionPacket::uuid,
            Action.STREAM_CODEC,
            SyncProgressOverlayExtensionPacket::action,
            ByteBufCodecs.optional(ProgressOverlayData.STREAM_CODEC),
            SyncProgressOverlayExtensionPacket::data,
            SyncProgressOverlayExtensionPacket::new
    );

    public static SyncProgressOverlayExtensionPacket register(BossEvent bossEvent, ProgressOverlayData<?> data) {
        return new SyncProgressOverlayExtensionPacket(bossEvent.getId(), Action.REGISTER, Optional.of(data));
    }

    public static SyncProgressOverlayExtensionPacket remove(BossEvent bossEvent) {
        return new SyncProgressOverlayExtensionPacket(bossEvent.getId(), Action.REMOVE, Optional.empty());
    }

    public static SyncProgressOverlayExtensionPacket update(BossEvent bossEvent, ProgressOverlayData<?> data) {
        return new SyncProgressOverlayExtensionPacket(bossEvent.getId(), Action.UPDATE, Optional.of(data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Action implements StringRepresentable {
        REGISTER,
        REMOVE,
        UPDATE;

        public static final Codec<Action> CODEC = StringRepresentable.fromValues(Action::values);
        public static final StreamCodec<ByteBuf, Action> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}
