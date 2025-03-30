package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.EntityDataType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record SyncEntityDataPayload(int entityId, HolderSet<EntityDataType<?>> entityDataTypes,
                                    CompoundTag data) implements CustomPacketPayload {
    public static final Type<SyncEntityDataPayload> TYPE = new Type<>(NarakaMod.location("sync_entity_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityDataPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncEntityDataPayload::entityId,
            ByteBufCodecs.holderSet(NarakaRegistries.Keys.ENTITY_DATA_TYPE),
            SyncEntityDataPayload::entityDataTypes,
            ByteBufCodecs.COMPOUND_TAG,
            SyncEntityDataPayload::data,
            SyncEntityDataPayload::new
    );

    public SyncEntityDataPayload(LivingEntity livingEntity, Holder<EntityDataType<?>> entityDataType, CompoundTag data) {
        this(livingEntity.getId(), HolderSet.direct(entityDataType), data);
    }

    public SyncEntityDataPayload(LivingEntity livingEntity, HolderSet<EntityDataType<?>> entityDataTypes, CompoundTag data) {
        this(livingEntity.getId(), entityDataTypes, data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncEntityDataPayload payload, NetworkManager.Context context) {
        Player player = context.player();
        Entity entity = player.level().getEntity(payload.entityId);
        CompoundTag data = payload.data;
        for (Holder<EntityDataType<?>> holder : payload.entityDataTypes) {
            EntityDataType<?> entityDataType = holder.value();
            Object value = entityDataType.read(data, context.registryAccess());
            if (entity instanceof LivingEntity livingEntity)
                EntityDataHelper.setEntityData(livingEntity, holder.value(), value);
        }
    }
}
