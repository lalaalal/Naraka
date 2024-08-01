package com.yummy.naraka.network;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.EntityDataType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record SyncEntityDataPayload(int entityId, Holder<EntityDataType<?>> entityDataType,
                                    CompoundTag data) implements CustomPacketPayload {
    public static final Type<SyncEntityDataPayload> TYPE = CustomPacketPayload.createType("sync_entity_data");

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityDataPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncEntityDataPayload::entityId,
            ByteBufCodecs.holderRegistry(NarakaRegistries.ENTITY_DATA_TYPE.key()),
            SyncEntityDataPayload::entityDataType,
            ByteBufCodecs.COMPOUND_TAG,
            SyncEntityDataPayload::data,
            SyncEntityDataPayload::new
    );

    public SyncEntityDataPayload(LivingEntity livingEntity, Holder<EntityDataType<?>> entityDataType, CompoundTag data) {
        this(livingEntity.getId(), entityDataType, data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(SyncEntityDataPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            LocalPlayer player = context.player();
            Entity entity = player.level().getEntity(payload.entityId);
            CompoundTag data = payload.data;
            EntityDataType<?> entityDataType = payload.entityDataType.value();
            Object value = entityDataType.read(data, player.registryAccess());
            if (entity instanceof LivingEntity livingEntity)
                EntityDataHelper.setEntityData(livingEntity, entityDataType, value);
        });
    }
}
