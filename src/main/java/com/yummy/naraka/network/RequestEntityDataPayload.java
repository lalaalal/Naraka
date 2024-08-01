package com.yummy.naraka.network;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.EntityDataType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record RequestEntityDataPayload(int entityId,
                                       Holder<EntityDataType<?>> entityDataType) implements CustomPacketPayload {
    public static final Type<RequestEntityDataPayload> TYPE = CustomPacketPayload.createType("request_entity_data");
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestEntityDataPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            RequestEntityDataPayload::entityId,
            ByteBufCodecs.holderRegistry(NarakaRegistries.ENTITY_DATA_TYPE.key()),
            RequestEntityDataPayload::entityDataType,
            RequestEntityDataPayload::new
    );

    public RequestEntityDataPayload(LivingEntity entity, Holder<EntityDataType<?>> entityDataType) {
        this(entity.getId(), entityDataType);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(RequestEntityDataPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        MinecraftServer server = context.server();
        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(payload.entityId);
            if (entity instanceof LivingEntity livingEntity) {
                CompoundTag data = new CompoundTag();
                EntityDataHelper.saveEntityData(livingEntity, data);
                ServerPlayNetworking.send(player, new SyncEntityDataPayload(payload.entityId, payload.entityDataType, data));
            }
        }
    }
}
