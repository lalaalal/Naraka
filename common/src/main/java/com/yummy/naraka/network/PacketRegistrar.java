package com.yummy.naraka.network;

public interface PacketRegistrar {
    <T extends CustomPacketPayload<T>> void define(CustomPacketPayload.Type<T> type);

    <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler);
}
