package com.yummy.naraka.fabric;

import com.yummy.naraka.network.ClientboundNetworkManager;
import com.yummy.naraka.network.CustomPacketPayload;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.PacketRegistrar;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkManager {
    public static class FabricServerPacketRegistrar implements PacketRegistrar.Server {
        @Override
        public <T extends CustomPacketPayload<T>> void define(CustomPacketPayload.Type<T> type) {
            ServerPlayNetworking.registerGlobalReceiver(FabricPacketProxy.createType(type), (proxy, player, sender) -> {
            });
        }

        @Override
        public <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler) {
            ServerPlayNetworking.registerGlobalReceiver(FabricPacketProxy.createType(type), (proxy, player, sender) -> {
                handler.handle(proxy.payload(), () -> player);
            });
        }
    }

    public static class FabricClientboundNetworkManager implements ClientboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload<T>> void send(ServerPlayer player, T payload) {
            ServerPlayNetworking.send(player, new FabricPacketProxy<>(payload));
        }
    }
}
