package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.data.EntityData;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record SyncEntityDataPacket(int entityId, List<EntityData<?>> entityData) implements CustomPacketPayload {
    public static final Type<SyncEntityDataPacket> TYPE = new Type<>(NarakaMod.location("sync_entity_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityDataPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncEntityDataPacket::entityId,
            EntityData.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SyncEntityDataPacket::entityData,
            SyncEntityDataPacket::new
    );

    public SyncEntityDataPacket(LivingEntity livingEntity, EntityData<?> entityData) {
        this(livingEntity.getId(), List.of(entityData));
    }

    public SyncEntityDataPacket(LivingEntity livingEntity, List<EntityData<?>> entityData) {
        this(livingEntity.getId(), entityData);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncEntityDataPacket payload, NetworkManager.Context context) {
        Player player = context.player();
        Entity entity = player.level().getEntity(payload.entityId);
        for (EntityData<?> data : payload.entityData) {
            if (entity instanceof LivingEntity livingEntity)
                EntityDataHelper.setEntityData(livingEntity, data);
        }
    }
}
