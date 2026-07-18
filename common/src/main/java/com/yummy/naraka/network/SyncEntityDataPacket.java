package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.EntityData;
import com.yummy.naraka.world.entity.data.EntityDataExtension;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;

import java.util.List;

public record SyncEntityDataPacket(int entityId, Action action, List<EntityData<?, ?>> entityData)
        implements CustomPacketPayload<SyncEntityDataPacket> {
    public static final Codec<SyncEntityDataPacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("entity_id").forGetter(SyncEntityDataPacket::entityId),
                    Action.CODEC.fieldOf("action").forGetter(SyncEntityDataPacket::action),
                    EntityData.CODEC.listOf().fieldOf("entity_data").forGetter(SyncEntityDataPacket::entityData)
            ).apply(instance, SyncEntityDataPacket::new)
    );

    public static final Type<SyncEntityDataPacket> TYPE = new CodecType<>(NarakaMod.location("sync_entity_data"),
            SyncEntityDataPacket.class,
            CODEC
    );

    public static SyncEntityDataPacket sync(Entity entity, Action action, List<EntityData<?, ?>> entityData) {
        return new SyncEntityDataPacket(entity.getId(), action, entityData);
    }

    public static SyncEntityDataPacket sync(Entity entity, Action action, EntityData<?, ?> entityData) {
        return sync(entity, action, List.of(entityData));
    }

    @Override
    public Type<SyncEntityDataPacket> type() {
        return TYPE;
    }

    public void handle(NetworkManager.Context context) {
        this.action.sync(this, context);
    }

    private static void loadEntityData(SyncEntityDataPacket packet, NetworkManager.Context context) {
        Entity entity = context.level().getEntity(packet.entityId());
        if (entity instanceof EntityDataExtension entityDataExtension)
            entityDataExtension.naraka$loadEntityData(packet.entityData());
    }

    private static void removeGivenEntityData(SyncEntityDataPacket packet, NetworkManager.Context context) {
        Entity entity = context.level().getEntity(packet.entityId());
        if (entity instanceof EntityDataExtension entityDataExtension)
            entityDataExtension.naraka$removeEntityData(packet.entityData());
    }

    public enum Action implements StringRepresentable {
        LOAD(SyncEntityDataPacket::loadEntityData),
        REMOVE_GIVEN(SyncEntityDataPacket::removeGivenEntityData);

        public static final Codec<Action> CODEC = StringRepresentable.fromEnum(Action::values);

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
