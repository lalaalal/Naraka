package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.Platform;
import com.yummy.naraka.neoforge.NarakaEventBus;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.proxy.MethodProxy;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

@SuppressWarnings("unused")
public final class NeoForgeNetworkManager implements NarakaEventBus {
    public static final String VERSION = "1";

    @MethodProxy(NetworkManager.class)
    public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        if (Platform.getInstance().getSide() == Platform.Side.SERVER)
            registerClientHandler(type, codec, empty());
    }

    @MethodProxy(NetworkManager.class)
    public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        if (Platform.getInstance().getSide() == Platform.Side.CLIENT)
            registerServerHandler(type, codec, empty());
    }

    private static <T extends CustomPacketPayload> NetworkManager.PacketHandler<T> empty() {
        return (payload, context) -> {
        };
    }

    @MethodProxy(NetworkManager.class)
    public static <T extends CustomPacketPayload> void registerServerHandler(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
        NARAKA_BUS.addListener(RegisterPayloadHandlersEvent.class, event -> {
            event.registrar(VERSION).playToServer(type, codec, wrap(handler));
        });
    }

    @MethodProxy(NetworkManager.class)
    public static <T extends CustomPacketPayload> void registerClientHandler(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
        NARAKA_BUS.addListener(RegisterPayloadHandlersEvent.class, event -> {
            event.registrar(VERSION).playToClient(type, codec, wrap(handler));
        });
    }

    private static <T extends CustomPacketPayload> IPayloadHandler<T> wrap(NetworkManager.PacketHandler<T> handler) {
        return (payload, context) -> {
            handler.handle(payload, context::player);
        };
    }

    @MethodProxy(NetworkManager.class)
    public static void sendToClient(ServerPlayer player, CustomPacketPayload packet) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    @MethodProxy(NetworkManager.class)
    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }
}
