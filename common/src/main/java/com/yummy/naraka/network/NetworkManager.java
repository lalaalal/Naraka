package com.yummy.naraka.network;

import com.yummy.naraka.proxy.MethodInvoker;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collection;

public abstract class NetworkManager {
    public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        MethodInvoker.invoke(NetworkManager.class, "registerS2C", type, codec);
    }

    public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        MethodInvoker.invoke(NetworkManager.class, "registerC2S", type, codec);
    }

    public static <T extends CustomPacketPayload> void registerServerHandler(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PacketHandler<T> handler) {
        MethodInvoker.invoke(NetworkManager.class, "registerServerHandler", type, codec, handler);
    }

    public static <T extends CustomPacketPayload> void registerClientHandler(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PacketHandler<T> handler) {
        MethodInvoker.invoke(NetworkManager.class, "registerClientHandler", type, codec, handler);
    }

    public static void sendToClient(ServerPlayer player, CustomPacketPayload packet) {
        MethodInvoker.invoke(NetworkManager.class, "sendToClient", player, packet);
    }

    public static void sendToClient(Collection<ServerPlayer> players, CustomPacketPayload packet) {
        for (ServerPlayer player : players)
            sendToClient(player, packet);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        MethodInvoker.invoke(NetworkManager.class, "sendToServer", payload);
    }

    @FunctionalInterface
    public interface PacketHandler<T extends CustomPacketPayload> {
        void handle(T value, Context context);
    }

    @FunctionalInterface
    public interface Context {
        Player player();

        default RegistryAccess registryAccess() {
            return player().registryAccess();
        }

        default Level level() {
            return player().level();
        }
    }
}
