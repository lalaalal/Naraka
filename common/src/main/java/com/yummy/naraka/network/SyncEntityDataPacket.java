package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.EntityData;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.UUID;

public record SyncEntityDataPacket(UUID uuid, Action action,
                                   List<EntityData<?, ?>> entityData) implements CustomPacketPayload {
    public static final Type<SyncEntityDataPacket> TYPE = new Type<>(NarakaMod.identifier("sync_entity_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityDataPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            SyncEntityDataPacket::uuid,
            Action.STREAM_CODEC,
            SyncEntityDataPacket::action,
            EntityData.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SyncEntityDataPacket::entityData,
            SyncEntityDataPacket::new
    );

    public static SyncEntityDataPacket sync(Entity entity, Action action, List<EntityData<?, ?>> entityData) {
        return new SyncEntityDataPacket(entity.getUUID(), action, entityData);
    }

    public static SyncEntityDataPacket sync(Entity entity, Action action, EntityData<?, ?> entityData) {
        return sync(entity, action, List.of(entityData));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(NetworkManager.Context context) {
        this.action.sync(this, context);
    }

    private static void loadEntityData(SyncEntityDataPacket packet, NetworkManager.Context context) {
        for (EntityData<?, ?> data : packet.entityData())
            EntityDataHelper.loadEntityData(context.level(), packet.uuid(), data);
    }

    private static void removeGivenEntityData(SyncEntityDataPacket packet, NetworkManager.Context context) {
        for (EntityData<?, ?> data : packet.entityData())
            EntityDataHelper.removeEntityData(context.level(), packet.uuid(), data.type());
    }

    private static void removeAllEntityData(SyncEntityDataPacket packet, NetworkManager.Context context) {
        EntityDataHelper.removeEntityData(context.level(), packet.uuid());
    }

    public enum Action implements StringRepresentable {
        LOAD(SyncEntityDataPacket::loadEntityData),
        REMOVE_GIVEN(SyncEntityDataPacket::removeGivenEntityData),
        REMOVE_ALL(SyncEntityDataPacket::removeAllEntityData);

        public static final Codec<Action> CODEC = StringRepresentable.fromEnum(Action::values);
        public static final StreamCodec<ByteBuf, Action> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        private final NetworkManager.PacketHandler<SyncEntityDataPacket> action;

        Action(NetworkManager.PacketHandler<SyncEntityDataPacket> action) {
            this.action = action;
        }

        public void sync(SyncEntityDataPacket packet, NetworkManager.Context context) {
            action.handle(packet, context);
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
