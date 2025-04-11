package com.yummy.naraka.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public interface ClientboundNetworkManager extends PacketHandlerRegistrar {
    void send(ServerPlayer player, CustomPacketPayload payload);

    default void send(Collection<ServerPlayer> players, CustomPacketPayload payload) {
        for (ServerPlayer player : players)
            send(player, payload);
    }
}
