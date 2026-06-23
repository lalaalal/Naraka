package com.yummy.naraka.network;

public interface PacketHandlerRegistrar {
    <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler);
}
