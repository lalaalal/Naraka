package com.yummy.naraka.fabric;

import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.proxy.MethodProxy;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class FabricNetworkManager {
    @MethodProxy(NetworkManager.class)
    public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.playS2C().register(type, codec);
    }

    @MethodProxy(NetworkManager.class)
    public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.playC2S().register(type, codec);
    }

    @MethodProxy(NetworkManager.class)
    public static <T extends CustomPacketPayload> void registerServerHandler(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            handler.handle(payload, context::player);
        });
    }

    @MethodProxy(NetworkManager.class)
    public static <T extends CustomPacketPayload> void registerClientHandler(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            handler.handle(payload, context::player);
        });
    }

    @MethodProxy(NetworkManager.class)
    public static void sendToClient(ServerPlayer player, CustomPacketPayload packet) {
        ServerPlayNetworking.send(player, packet);
    }

    @MethodProxy(NetworkManager.class)
    public static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
