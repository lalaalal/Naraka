package com.yummy.naraka.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ServerboundNetworkManager extends PacketHandlerRegistrar {
    void send(CustomPacketPayload payload);
}
