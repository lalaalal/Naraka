package com.yummy.naraka.fabric;

import com.yummy.naraka.network.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

public class FabricPacketProxy<T extends CustomPacketPayload<T>> implements FabricPacket {
    private final PacketType<FabricPacketProxy<T>> fabricPacketType;
    private final T payload;

    public FabricPacketProxy(T payload) {
        this.payload = payload;
        this.fabricPacketType = createType(payload.type());
    }

    public static <T extends CustomPacketPayload<T>> PacketType<FabricPacketProxy<T>> createType(CustomPacketPayload.Type<T> type) {
        return PacketType.create(type.id(), buf -> new FabricPacketProxy<>(type.decode(buf)));
    }

    public T payload() {
        return payload;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        payload.type().encode(payload, buf);
    }

    @Override
    public PacketType<?> getType() {
        return fabricPacketType;
    }
}
