package com.yummy.naraka.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public interface ClientboundNetworkManager extends PacketHandlerRegistrar {
    <T extends CustomPacketPayload<T>> void define(CustomPacketPayload.Type<T> type);

    <T extends CustomPacketPayload<T>> void send(ServerPlayer player, T payload);

    default <T extends CustomPacketPayload<T>> void send(Collection<ServerPlayer> players, T payload) {
        for (ServerPlayer player : players)
            send(player, payload);
    }

    default void send(ServerPlayer player, Packet<?> packet) {
        player.connection.send(packet);
    }

    default void send(Collection<ServerPlayer> players, Packet<?> packet) {
        for (ServerPlayer player : players)
            send(player, packet);
    }
}
