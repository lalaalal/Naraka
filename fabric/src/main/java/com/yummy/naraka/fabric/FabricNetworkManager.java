package com.yummy.naraka.fabric;

import com.yummy.naraka.network.ClientboundNetworkManager;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.ServerboundNetworkManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class FabricNetworkManager {
    public static class FabricServerboundNetworkManager implements ServerboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload> void define(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
            PayloadTypeRegistry.serverboundPlay().register(type, codec);
        }

        @Override
        public <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
            ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                handler.handle(payload, context::player);
            });
        }

        @Override
        public void send(CustomPacketPayload payload) {
            ClientPlayNetworking.send(payload);
        }
    }

    public static class FabricClientboundNetworkManager implements ClientboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload> void define(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
            PayloadTypeRegistry.clientboundPlay().register(type, codec);
        }

        @Override
        public <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
            ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                handler.handle(payload, context::player);
            });
        }

        @Override
        public void send(ServerPlayer player, CustomPacketPayload payload) {
            ServerPlayNetworking.send(player, payload);
        }
    }
}
