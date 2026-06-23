package com.yummy.naraka.network;

public interface ServerboundNetworkManager extends PacketHandlerRegistrar {
    <T extends CustomPacketPayload<T>> void send(T payload);
}
