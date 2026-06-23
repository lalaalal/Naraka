package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.overlay.ProgressOverlayData;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.BossEvent;

import java.util.Optional;
import java.util.UUID;

public record SyncProgressOverlayExtensionPacket(UUID uuid, Action action, Optional<ProgressOverlayData<?>> data)
        implements CustomPacketPayload<SyncProgressOverlayExtensionPacket> {
    public static final Codec<SyncProgressOverlayExtensionPacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(SyncProgressOverlayExtensionPacket::uuid),
                    Action.CODEC.fieldOf("action").forGetter(SyncProgressOverlayExtensionPacket::action),
                    ProgressOverlayData.CODEC.optionalFieldOf("data").forGetter(SyncProgressOverlayExtensionPacket::data)
            ).apply(instance, SyncProgressOverlayExtensionPacket::new)
    );

    public static final Type<SyncProgressOverlayExtensionPacket> TYPE = new CodecType<>(
            NarakaMod.location("sync_progress_overlay_extension"),
            SyncProgressOverlayExtensionPacket.class,
            CODEC
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
    public Type<SyncProgressOverlayExtensionPacket> type() {
        return TYPE;
    }

    public enum Action implements StringRepresentable {
        REGISTER,
        REMOVE,
        UPDATE;

        public static final Codec<Action> CODEC = StringRepresentable.fromEnum(Action::values);

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}
