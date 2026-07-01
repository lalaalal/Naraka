package com.yummy.naraka.fabric;

import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.network.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class FabricNetworkManager {
    private static final ClientboundNetworkManager CLIENTBOUND = new FabricClientboundNetworkManager();
    private static final PacketRegistrar SERVER_PACKET_REGISTRAR = new FabricServerPacketRegistrar();

    @MethodProxy(NetworkManager.class)
    public static ClientboundNetworkManager clientbound() {
        return CLIENTBOUND;
    }

    @MethodProxy(NarakaNetworks.class)
    public static PacketRegistrar getServerPacketRegistrar() {
        return SERVER_PACKET_REGISTRAR;
    }

    private static class FabricServerPacketRegistrar implements PacketRegistrar {
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

    private static class FabricClientboundNetworkManager implements ClientboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload<T>> void send(ServerPlayer player, T payload) {
            ServerPlayNetworking.send(player, new FabricPacketProxy<>(payload));
        }
    }
}
