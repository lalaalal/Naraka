package com.yummy.naraka.fabric.client;

import com.yummy.naraka.fabric.FabricPacketProxy;
import com.yummy.naraka.network.CustomPacketPayload;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.PacketRegistrar;
import com.yummy.naraka.network.ServerboundNetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public final class FabricClientNetworkManager {
    public static class FabricServerboundNetworkManager implements ServerboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload<T>> void send(T payload) {
            ClientPlayNetworking.send(new FabricPacketProxy<>(payload));
        }
    }

    public static class FabricClientPacketRegistrar implements PacketRegistrar.Client {
        @Override
        public <T extends CustomPacketPayload<T>> void define(CustomPacketPayload.Type<T> type) {

        }

        @Override
        public <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler) {
            ClientPlayNetworking.registerGlobalReceiver(FabricPacketProxy.createType(type), (proxy, player, sender) -> {
                handler.handle(proxy.payload(), () -> player);
            });
        }
    }
}
