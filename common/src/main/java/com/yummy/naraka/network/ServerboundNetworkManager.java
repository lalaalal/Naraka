package com.yummy.naraka.network;

public interface ServerboundNetworkManager {
    <T extends CustomPacketPayload<T>> void send(T payload);
}
